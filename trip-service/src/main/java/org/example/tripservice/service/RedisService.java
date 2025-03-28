package org.example.tripservice.service;

import com.redis.lettucemod.api.StatefulRedisModulesConnection;
import com.redis.lettucemod.api.async.RedisModulesAsyncCommands;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.example.tripservice.dto.TripDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisService {

    private final GenericObjectPool<StatefulRedisModulesConnection<String, String>> pool;

    public void storeTrip(String key,String tripDetails){

        try (StatefulRedisModulesConnection<String, String> connection = pool.borrowObject()) { // (3)
            RedisModulesAsyncCommands<String, String> commands = connection.async(); // (4)

            commands.jsonSet(key, "$", tripDetails);
             // ...
        } catch (Exception e) {
            log.error("Could not get a connection from the pool", e);
        }
    }

}
