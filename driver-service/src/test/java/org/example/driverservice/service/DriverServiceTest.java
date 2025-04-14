package org.example.driverservice.service;

import com.redis.lettucemod.api.StatefulRedisModulesConnection;
import io.lettuce.core.GeoWithin;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static reactor.core.publisher.Mono.when;



@ExtendWith(MockitoExtension.class)
class DriverServiceTest {
    @Mock
    private GenericObjectPool<StatefulRedisModulesConnection<String, String>> redisConnectionPool;

    @Mock
    private StatefulRedisModulesConnection<String, String> redisConnection;

    @InjectMocks
    private DriverService driverService;

    @Test
    void driversAreAvailable() throws Exception {
        Location location = new Location();
        location.setCoordinate(new Coordinate(77.5, 12.9));
        int radiusKm = 60;
        GeoWithin<String> d1 = new GeoWithin<>("drivers:1",1.0,null,null);
        GeoWithin<String> d2 = new GeoWithin<>("drivers:1",1.0,null,null);

        List<GeoWithin<String>> drivers = Arrays.asList(d1,d2);







    }
    @Test
    void driversAreNotAvailable() {

    }
    @Test
    void driverExceptionFailed() {}

}