package org.example.driverservice.service;

import com.redis.lettucemod.api.StatefulRedisModulesConnection;
import com.redis.lettucemod.api.async.RedisModulesAsyncCommands;
import io.lettuce.core.GeoArgs;
import io.lettuce.core.GeoSearch;
import io.lettuce.core.GeoWithin;
import io.lettuce.core.RedisFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.example.driverservice.NoDriversAvailableException;
import org.example.driverservice.exception.AssignDriverException;
import org.example.sharedlibs.avro.Location;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.example.driverservice.service.AssignmentService.LOCK_DRIVER;

@Service
@RequiredArgsConstructor
@Slf4j                              
public class DriverService {

    private static final String GEO_ZSET = "drivers:geo";
    private final RedissonClient redissonClient;
    private static final String Available_HASH = "drivers:available";
    private final GenericObjectPool<StatefulRedisModulesConnection<String, String>> redisConnectionPool;

    public Optional<GeoWithin<String>> findAndReserveDriver(Location location, int radiusKm){
        try (StatefulRedisModulesConnection<String, String> conn = redisConnectionPool.borrowObject()) {
            RedisModulesAsyncCommands<String, String> commands = conn.async();

            // 1. Find nearby drivers
            List<GeoWithin<String>> drivers = commands.geosearch(
                    GEO_ZSET,
                    GeoSearch.fromCoordinates(location.getCoordinate().getLongitude(),
                            location.getCoordinate().getLatitude()),
                    GeoSearch.byRadius(radiusKm, GeoArgs.Unit.km),
                    new GeoArgs().withDistance().asc()
            ).get(3, TimeUnit.SECONDS);

            System.out.println(drivers);

            if(drivers.isEmpty()){
                return Optional.empty();
            }

            // 2. Try reserving first available driver
            for (GeoWithin<String> driver : drivers) {
                if(tryReserveDriver(driver)){
                    return Optional.of(driver);
                }
            }
            return Optional.empty();


            } catch (TimeoutException tx){
            throw new AssignDriverException("Driver Search Timeout",tx.getMessage());
        } catch (Exception e) {
            throw new AssignDriverException("Driver Search Failed",e.getMessage());
        }
    }
    public boolean tryReserveDriver(GeoWithin<String> driver){
        RLock lock = redissonClient.getLock( LOCK_DRIVER+":"+driver.getMember());
        try {
            //short lock for reservation of driver
            if(lock.tryLock(50,500,TimeUnit.MILLISECONDS)){
                System.out.println("locked" + lock.isLocked());
                System.out.println("driver reserved " + driver);
                try(StatefulRedisModulesConnection<String, String> conn = redisConnectionPool.borrowObject()){
                    Long removed = conn.sync().hdel(Available_HASH, driver.getMember());
                    System.out.println("removed " + removed);
                    return removed != null && removed > 0;
                }
            }
            return false;
        }catch (InterruptedException itx){
            Thread.currentThread().interrupt();
            return false;
        }catch (Exception e){
            throw new AssignDriverException("Driver Reservation Failed",e.getMessage());
        }finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }


    public  GeoWithin<String>  findAvailableDriverNearLocation(Location location, int radiusKm) {

        try (StatefulRedisModulesConnection<String, String> conn = redisConnectionPool.borrowObject()) {
            RedisModulesAsyncCommands<String, String> commands = conn.async();

            // Fetch nearby drivers sorted by proximity
            List<GeoWithin<String>> nearbyDrivers = commands.geosearch(
                    GEO_ZSET,
                    GeoSearch.fromCoordinates(location.getCoordinate().getLongitude(),
                            location.getCoordinate().getLatitude()),
                    GeoSearch.byRadius(radiusKm, GeoArgs.Unit.km),
                    new GeoArgs().withDistance().asc()).get();

            if (nearbyDrivers.isEmpty()) {
                throw new NoDriversAvailableException("No drivers in vicinity");
            }

            // Parallelize availability checks while preserving order
            List<RedisFuture<Boolean>> availabilityChecks = nearbyDrivers.stream()
                    .map(driver -> commands.hexists(Available_HASH, driver.getMember())).toList();

            for (int i = 0; i < nearbyDrivers.size(); i++) {
                GeoWithin<String> driver = nearbyDrivers.get(i);
                try {
                    if (availabilityChecks.get(i).get(500, TimeUnit.MILLISECONDS)) {
                        log.debug("driver found" + driver.getMember());
                        return  driver ;
                    }
                } catch (TimeoutException tex) {
                    log.warn("Timeout verfing aval of driver " + driver.getMember());
                } catch (Exception ex) {
                    log.warn("Error checking driver {}: {}", driver.getMember(), ex.getMessage());
                }
            }
            throw new NoDriversAvailableException("No available drivers found");

        } catch (InterruptedException intex) {
            Thread.currentThread().interrupt();
            throw new AssignDriverException("Operation interrupted " + intex.getMessage());
        } catch (Exception e) {
            if(e.getCause() instanceof NoDriversAvailableException){
                throw (NoDriversAvailableException)e.getCause();
            }
            log.error("Find Driver Exception {}", e.getMessage());
            throw new AssignDriverException("Driver assignment error--00---"+ e.getMessage());
        }
    }


    public void releaseDriver(String driverId) {
        try (StatefulRedisModulesConnection<String, String> conn = redisConnectionPool.borrowObject()) {
            conn.sync().hset(Available_HASH, driverId, "available");
        } catch (Exception e) {
            log.error("Failed to release driver {}", driverId, e);
        }
    }

}
