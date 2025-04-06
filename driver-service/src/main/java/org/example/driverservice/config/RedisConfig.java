package org.example.driverservice.config;

import com.redis.lettucemod.RedisModulesClient;
import com.redis.lettucemod.api.StatefulRedisModulesConnection;
import io.lettuce.core.support.ConnectionPoolSupport;
import jakarta.annotation.PostConstruct;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.example.driverservice.redlock.RedLock;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
public class RedisConfig {
    @Value("${app.redis.host}")
    String host;

    @Value("${app.redis.port}")
    String port;

    @Value("${app.redis.password}")
    String password;


    String redisUrl;


    @PostConstruct
    public void init() {
        System.out.println("Redis Host: " + host);
        System.out.println("Redis Port: " + port);
        System.out.println("Redis Password: " + password);
        redisUrl = String.format("redis://:%s@%s:%s", password, host, port);

        System.out.println("✅ Final Redis URL: " + redisUrl);
    }
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

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://server.vinayakpaste.site:6379")
                .setUsername("default")
                .setPassword("admin");
        return Redisson.create(config);
    }

}
