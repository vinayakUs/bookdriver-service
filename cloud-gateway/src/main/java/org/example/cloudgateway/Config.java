package org.example.cloudgateway;

import org.apache.catalina.filters.CorsFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;

import java.util.List;

@Configuration
public class Config {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("location-service", r -> r.path("/api/location/**")
                        .uri("http://localhost:8083"))
                .build();
    }
    @Bean
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }



}
