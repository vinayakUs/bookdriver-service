package org.example.tripservice.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.tripservice.CustomJacksonHashMapper;
import org.example.tripservice.TripRequest;
import org.example.tripservice.dto.TripResponseDTO;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.hash.Jackson2HashMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TripService {
    private final KafkaProducerService kafkaProducerService;
    private final RedisTemplate<String,Object> restTemplate;
    private final Jackson2HashMapper jackson2HashMapper;
    private HashOperations<String, String, Object> hashOperations;
    private final CustomJacksonHashMapper<TripRequest> customJacksonHashMapper;



    @PostConstruct
    public void init() {
        this.hashOperations = restTemplate.opsForHash();
    }

    public TripResponseDTO requestTrip(TripRequest request) {
        String tripId = UUID.randomUUID().toString();

        TripResponseDTO response =  TripResponseDTO.builder()
                .uuid(tripId)
                .errorCode(null)
                .errorKey(null)
                .plusOne(null)
                .riderHasCompletedTrips(true)  // Assuming true for now
                .build();


        // Convert to Hash and store
        Map<String, Object> tripData = jackson2HashMapper.toHash(request);

        Map<String,Object> m = customJacksonHashMapper.toHash(request);

        hashOperations.putAll("KEY_TRIP:" + tripId, m);

        kafkaProducerService.publishTripEvent(request,tripId);

        return  response;
    }
}
