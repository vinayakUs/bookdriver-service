package org.example.driverservice.service;

import com.redis.lettucemod.api.StatefulRedisModulesConnection;
import com.redis.lettucemod.api.async.RedisModulesAsyncCommands;
import io.lettuce.core.GeoArgs;
import io.lettuce.core.GeoSearch;
import io.lettuce.core.GeoWithin;
import lombok.RequiredArgsConstructor;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.kafka.common.protocol.types.Field;
import org.example.sharedlibs.avro.Location;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Future;

@Service
@RequiredArgsConstructor
public class DriverService {

    private static final String GEO_ZSET = "drivers:geo";

    private static final String Available_HASH = "drivers:available:";


    private final GenericObjectPool<StatefulRedisModulesConnection<String,String>> redisConnectionPool;

    private List<GeoWithin<String>> findAvailableDriverNearLocation(Location location,int radiusKm){

        try(StatefulRedisModulesConnection<String,String> conn = redisConnectionPool.borrowObject()){
            RedisModulesAsyncCommands<String, String> commands = conn.async();

            // Find all drivers in Radius
              Future<List<GeoWithin<String>>> driverFuture= commands.geosearch(GEO_ZSET ,
                    GeoSearch.fromCoordinates(location.getCoordinate().getLongitude(),location.getCoordinate().getLatitude()),
                    GeoSearch.byRadius(radiusKm, GeoArgs.Unit.km),
                    new GeoArgs().withDistance().asc()
                    );
              List<GeoWithin<String>> drivers = driverFuture.get();

              drivers.stream().forEach(driver -> {
                  commands.hexists()
              });


        }catch (Exception e){}




    }

}




//
//
//@Service
//@Slf4j
//public class DriverAvailabilityService {
//
//    private static final String GEO_ZSET = "drivers:geo";
//    private static final String AVAILABLE_HASH = "drivers:available";
//    private static final String DRIVER_PREFIX = "driver:";
//
//    private final GenericObjectPool<StatefulRedisModulesConnection<String, String>> pool;
//
//    // Find available drivers near location
//    public List<GeoWithin<String>> findAvailableDrivers(Location location, int radiusKm)
//            throws NoDriversAvailableException {
//
//        try (StatefulRedisModulesConnection<String, String> conn = pool.borrowObject()) {
//            RedisModulesAsyncCommands<String, String> commands = conn.async();
//
//            // 1. Find all drivers in radius
//            Future<List<GeoWithin<String>>> futureDrivers = commands.geosearch(
//                    GEO_ZSET,
//                    GeoSearch.fromCoordinates(location.getLongitude(), location.getLatitude()),
//                    GeoSearch.byRadius(radiusKm, GeoArgs.Unit.km),
//                    new GeoArgs().withDistance().asc()
//            );
//
//            List<GeoWithin<String>> drivers = futureDrivers.get(2, TimeUnit.SECONDS);
//
//            // 2. Filter available drivers
//            List<GeoWithin<String>> availableDrivers = new ArrayList<>();
//            for (GeoWithin<String> driver : drivers) {
//                String driverId = driver.getMember();
//                Future<Boolean> isAvailable = commands.hexists(AVAILABLE_HASH, driverId);
//                if (isAvailable.get(500, TimeUnit.MILLISECONDS)) {
//                    availableDrivers.add(driver);
//                    if (availableDrivers.size() >= 10) break; // Limit results
//                }
//            }
//
//            if (availableDrivers.isEmpty()) {
//                throw new NoDriversAvailableException("No available drivers in " + radiusKm + "km radius");
//            }
//            return availableDrivers;
//        } catch (TimeoutException e) {
//            throw new NoDriversAvailableException("Driver search timeout");
//        } catch (Exception e) {
//            log.error("Driver search failed", e);
//            throw new NoDriversAvailableException("Search error");
//        }
//    }
//
//    // Update driver location (atomic)
//    public void updateDriverLocation(String driverId, double longitude, double latitude) {
//        try (StatefulRedisModulesConnection<String, String> conn = pool.borrowObject()) {
//            conn.sync().geoadd(GEO_ZSET, longitude, latitude, driverId);
//        } catch (Exception e) {
//            log.error("Location update failed for driver {}", driverId, e);
//        }
//    }
//
//    // Mark driver as booked
//    public boolean bookDriver(String driverId) {
//        try (StatefulRedisModulesConnection<String, String> conn = pool.borrowObject()) {
//            Long removed = conn.sync().hdel(AVAILABLE_HASH, driverId);
//            conn.sync().hset(DRIVER_PREFIX + driverId, "status", "booked");
//            return removed > 0;
//        } catch (Exception e) {
//            log.error("Booking failed for driver {}", driverId, e);
//            return false;
//        }
//    }
//
//    // Mark driver as available
//    public boolean releaseDriver(String driverId) {
//        try (StatefulRedisModulesConnection<String, String> conn = pool.borrowObject()) {
//            conn.sync().hset(AVAILABLE_HASH, driverId, "1");
//            conn.sync().hset(DRIVER_PREFIX + driverId,
//                    Map.of("status", "available", "last_update", String.valueOf(System.currentTimeMillis())));
//            return true;
//        } catch (Exception e) {
//            log.error("Release failed for driver {}", driverId, e);
//            return false;
//        }
//    }
//}