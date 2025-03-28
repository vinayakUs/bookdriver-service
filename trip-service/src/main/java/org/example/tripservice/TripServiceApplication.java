package org.example.tripservice;

import com.redis.lettucemod.api.StatefulRedisModulesConnection;
import com.redis.lettucemod.api.async.RedisModulesAsyncCommands;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class TripServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TripServiceApplication.class, args);
    }

    @Autowired
    private GenericObjectPool<StatefulRedisModulesConnection<String, String>> pool;



    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return new CommandLineRunner() {

            @Override
            public void run(String... args) throws Exception {
                // Example operations
                System.out.println("=== RedisJSON CLI ===");
//                try (StatefulRedisModulesConnection<String, String> connection = pool.borrowObject()) { // (3)
//                    RedisModulesAsyncCommands<String, String> commands = connection.async(); // (4)
//
//                    commands.jsonSet("user:22", "$",
//                            "{\"name\":\"John\",\"age\":30,\"address\":{\"city\":\"New York\"}}");
//                    System.out.println("Set user:1");
//                    // ...
//                } catch (Exception e) {
//                    log.error("Could not get a connection from the pool", e);
//                }
            }
        };
    }

}

