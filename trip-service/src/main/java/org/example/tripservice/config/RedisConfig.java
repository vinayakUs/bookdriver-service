package org.example.tripservice.config;

import com.redis.lettucemod.RedisModulesClient;
import com.redis.lettucemod.api.StatefulRedisModulesConnection;
import io.lettuce.core.support.ConnectionPoolSupport;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {

//    @Bean
//    public RedisTemplate<?,?> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
//        RedisTemplate<?,?> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//
//        return redisTemplate;
//    }
    @Value("${spring.data.redis.password}")
    String redisPassword;
    @Value("${spring.data.redis.port}")
            String redisPort;
    @Value("${spring.data.redis.host}")
            String redisHost;

    String redisUrl="redis://nE8GGRHl9OVCkJIcdFel5WcslHn6bDh2@redis-19507.c277.us-east-1-3.ec2.redns.redis-cloud.com:19507";
    @Bean(destroyMethod = "close")
    public GenericObjectPool<StatefulRedisModulesConnection<String, String>> redisPool() {
        RedisModulesClient client = RedisModulesClient.create(redisUrl);

        GenericObjectPoolConfig<StatefulRedisModulesConnection<String, String>> poolConfig =
                new GenericObjectPoolConfig<>();

        poolConfig.setMaxTotal(20);       // Maximum active connections
        poolConfig.setMaxIdle(10);        // Maximum idle connections
        poolConfig.setMinIdle(1);         // Minimum idle connections
        poolConfig.setTestOnBorrow(true); // Validate connection on borrow
        poolConfig.setJmxEnabled(false);

        return ConnectionPoolSupport.createGenericObjectPool(
                client::connect, // Connection supplier
                poolConfig
        );
    }

}
