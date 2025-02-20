package org.example.locationservice.config;

import com.google.maps.GeoApiContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class CustomBeans {
    @Value("${app.apis.google.maps.key}")
    private String googleMapsKey;

    @Bean
    @Scope("singleton")
    public GeoApiContext geoApiContext() {
        return  new GeoApiContext.Builder().apiKey(googleMapsKey).build();
    }

}
