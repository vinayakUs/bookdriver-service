package org.example.driverservice.service;

import com.redis.lettucemod.api.StatefulRedisModulesConnection;
import com.redis.lettucemod.api.async.RedisModulesAsyncCommands;
import io.lettuce.core.GeoArgs;
import io.lettuce.core.GeoSearch;
import io.lettuce.core.GeoWithin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.example.driverservice.NoDriversAvailableException;
import org.example.driverservice.exception.AssignDriverException;
import org.example.driverservice.redlock.RedLock;
import org.example.sharedlibs.avro.Location;
import org.example.sharedlibs.avro.TripDetails;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class AssignmentService {

    private final GenericObjectPool<StatefulRedisModulesConnection<String, String>> pool;
//    private final RedLock redLock;
    private final DriverService driverService;
    private final RedissonClient redisson;

    public List<GeoWithin<String>> findNearestDriver(Location location) {

        try (StatefulRedisModulesConnection<String, String> conn = pool.borrowObject()) {
            RedisModulesAsyncCommands<String, String> commands = conn.async();
            GeoArgs geoArgs = new GeoArgs().withDistance().asc();
            Future<List<GeoWithin<String>>> futureResult = commands.geosearch("drivers", GeoSearch.fromCoordinates(location.getCoordinate().getLongitude(), location.getCoordinate().getLatitude()),
                    GeoSearch.byRadius(50, GeoArgs.Unit.km),
                    geoArgs
            );


            return futureResult.get();

        } catch (Exception e) {
            log.error("Could not get a connection from the pool", e);
        }
        return null;
    }
private final RedissonClient redissonClient;


    public void assignDriver(TripDetails tripDetails) {

        RLock lock = redissonClient.getLock("lock_" + "User:s93no-3f93ni-dd34e2");
        try{
            // try to get lock on user
            boolean userLock = lock.tryLock(0 ,60 , TimeUnit.SECONDS);
            if(!userLock){

                log.info("User lock failed for user" + "user");
                throw new AssignDriverException("User lock failed for id --");

            }else {
                log.info("User lock acquired for user" + "user");
            }

            // 2. Find nearest drivers (limited to 10 for efficiency)
            List<GeoWithin<String>> _driverList =
                    driverService.findAvailableDriverNearLocation(tripDetails.getDestination(),50);
            List<GeoWithin<String>> driverList = new ArrayList<>();
            _driverList.forEach(driver -> {
                if(driver != null){
                    driverList.add(driver);
                }
            });
            //            List<>
//            List<>

            // 3. Try assigning the first available driver
            System.out.println("driverList: " + driverList);

            for(GeoWithin<String> driver : driverList){


                String driverId = driver.getMember();
                RLock driverRLock = redissonClient.getLock("lock_"+driver.getMember());

                try{
                    //try to acquire driver lock (Non Blocking 0)
                    boolean driverLock = driverRLock.tryLock(0 ,60 , TimeUnit.SECONDS);
                    if(driverLock){
                        //assign driver
                        System.out.println("Driver lock acquired for " + driverId);
                        break;
                    }


                }finally {
                    if(driverRLock.isHeldByCurrentThread()){
                        driverRLock.unlock();
                    }
                }


            }


        }catch (Exception e){
            log.error("Could not assign driver", e);
        }
        finally {
            if(lock.isHeldByCurrentThread()){
                lock.unlock();
            }
        }
    }
}





