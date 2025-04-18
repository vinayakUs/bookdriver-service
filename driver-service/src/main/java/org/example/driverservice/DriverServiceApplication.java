package org.example.driverservice;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class DriverServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DriverServiceApplication.class, args);
    }

    @Autowired
    private RedissonClient redissonClient;

  @Bean
    public CommandLineRunner init(ApplicationContext applicationContext) {
        return new CommandLineRunner() {

            @Override
            public void run(String... args) throws Exception {

            }
        };
  }
    

}
