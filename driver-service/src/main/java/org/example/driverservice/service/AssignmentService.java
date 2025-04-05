package org.example.driverservice.service;

import com.redis.lettucemod.api.StatefulRedisModulesConnection;
import com.redis.lettucemod.api.async.RedisModulesAsyncCommands;
import io.lettuce.core.GeoArgs;
import io.lettuce.core.GeoSearch;
import io.lettuce.core.GeoWithin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.example.driverservice.RedisLock.RedisLock;
import org.example.sharedlibs.avro.Location;
import org.example.sharedlibs.avro.TripDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Future;

@Service
@Slf4j
@RequiredArgsConstructor
public class AssignmentService {

    private final GenericObjectPool<StatefulRedisModulesConnection<String, String>> pool;

    public List<GeoWithin<String>> findNearestDriver(Location location) {

        try(StatefulRedisModulesConnection<String,String> conn = pool.borrowObject()) {
            RedisModulesAsyncCommands<String, String> commands = conn.async();
            GeoArgs geoArgs = new GeoArgs().withDistance().asc();
            Future<List<GeoWithin<String>>> futureResult= commands.geosearch("drivers", GeoSearch.fromCoordinates(location.getCoordinate().getLongitude(),location.getCoordinate().getLatitude()),
                    GeoSearch.byRadius(50,GeoArgs.Unit.km),
                    geoArgs
                    );

            return futureResult.get();


        } catch (Exception e) {
            log.error("Could not get a connection from the pool", e);
        }

return null;
    }

    private final RedisLock redisLock;

    public void assignDriver(TripDetails tripDetails) {
       List<GeoWithin<String >> lod= findNearestDriver(tripDetails.getDestination());

        System.out.println("selected driver " + lod.get(0));






    }
}
