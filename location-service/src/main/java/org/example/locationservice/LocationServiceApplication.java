package org.example.locationservice;

import com.google.maps.PlacesApi;
import com.google.maps.model.AutocompletePrediction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import com.google.maps.GeoApiContext;

import java.util.Arrays;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableDiscoveryClient
public class LocationServiceApplication implements CommandLineRunner {

    @Value("${app.apis.google.maps.key}")
    String apiKey;

    @Autowired
    private GeoApiContext context;


    public static void main(String[] args) {
        SpringApplication.run(LocationServiceApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
//        AutocompletePrediction[] predictions = PlacesApi.queryAutocomplete(context, "Talegaon").await();
////        PlacesApi.placeAutocomplete()
//
//        System.out.println(Arrays.toString(predictions));
    }
}
