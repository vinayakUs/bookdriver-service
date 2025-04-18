package org.example.gateway;

import java.beans.BeanProperty;

import org.example.sharedlibs.avro.TripDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.example.sharedlibs.payload.ApiResponseDto;

@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
    @Autowired
    private ReactiveRedisTemplate<String, Long> redisTemplate;



    @Bean
    public CommandLineRunner commandLineRunner() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                // This method will be executed after the application context is loaded
                System.out.println("Gateway application started successfully!");
                ApiResponseDto<String> a = new ApiResponseDto<>(true,"sd");
            }
        };
    }

}
