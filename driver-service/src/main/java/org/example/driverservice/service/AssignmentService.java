package org.example.driverservice.service;

import com.redis.lettucemod.api.StatefulRedisModulesConnection;
import com.redis.lettucemod.api.async.RedisModulesAsyncCommands;
import io.lettuce.core.GeoArgs;
import io.lettuce.core.GeoSearch;
import io.lettuce.core.GeoWithin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.example.sharedlibs.Location;
import org.example.sharedlibs.TripDetails;
import org.springframework.data.redis.domain.geo.GeoReference;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AssignmentService {

    private final GenericObjectPool<StatefulRedisModulesConnection<String, String>> pool;

    public void findNearestDriver(Location location) {

        try(StatefulRedisModulesConnection<String,String> conn = pool.borrowObject()) {
            RedisModulesAsyncCommands<String, String> commands = conn.async();
            GeoArgs geoArgs = new GeoArgs().withDistance().asc();
            Future<List<GeoWithin<String>>> futureResult= commands.geosearch("drivers", GeoSearch.fromCoordinates(location.getCoordinate().getLongitude(),location.getCoordinate().getLatitude()),
                    GeoSearch.byRadius(50,GeoArgs.Unit.km),
                    geoArgs
                    );

            System.out.println(futureResult.get());

        } catch (Exception e) {
            log.error("Could not get a connection from the pool", e);
        }


    }


    public void assignDriver(TripDetails tripDetails) {

    }
}
