package org.example.tripservice;

import com.redis.lettucemod.api.StatefulRedisModulesConnection;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.example.sharedlibs.Test123;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
@Slf4j
public class TripServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TripServiceApplication.class, args);
    }

    @Autowired
    private GenericObjectPool<StatefulRedisModulesConnection<String, String>> pool;
    @Autowired
    private KafkaTemplate<String, Test123> kafkaTemplate;


    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return new CommandLineRunner() {

            @Override
            public void run(String... args) throws Exception {
                // Example operations
//                System.out.println("=== RedisJSON CLI ===");
//                Test123 test123 = new Test123();
//                test123.setName("sdsdsd");
//
//                kafkaTemplate.send("TRIP_REQUEST_EVENT", test123);
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

