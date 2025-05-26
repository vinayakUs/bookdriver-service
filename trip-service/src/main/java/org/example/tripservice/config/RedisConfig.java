package org.example.tripservice.config;

import com.redis.lettucemod.RedisModulesClient;
import com.redis.lettucemod.api.StatefulRedisModulesConnection;

import io.lettuce.core.RedisURI;
import io.lettuce.core.support.ConnectionPoolSupport;
import jakarta.annotation.PostConstruct;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {
    @Value("${app.redis.host}")
    String redisHost;

    @Value("${app.redis.port}")
    Integer redisPort;

    @Value("${app.redis.password}")
    String redisPassword;

    @Value("${app.redis.username}")
    String redisUsername;

    String redisUrl;
//    = String.format("redis://default:%s@%s:%s",password,host,port) ;


    @PostConstruct
    public void init() {
        System.out.println("Redis Host: " + redisHost);
        System.out.println("Redis Port: " + redisPort);
        System.out.println("Redis Password: " + redisPassword);
      //  redisUrl = String.format("redis://%s@%s:%s", password, host, redisPort);

      //  
    }
    @Bean(destroyMethod = "close")
    public GenericObjectPool<StatefulRedisModulesConnection<String, String>> redisPool() {
        
    // Create RedisURI with username and password
    RedisURI redisUrl = RedisURI.Builder.redis(redisHost, redisPort)
            .withAuthentication(redisUsername, redisPassword.toCharArray())
            .build();
        
            System.out.println("✅ Final Redis URL: " + redisUrl);
        
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
