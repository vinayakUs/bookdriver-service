package org.example.tripservice.service;

import lombok.RequiredArgsConstructor;
import org.example.tripservice.TripRequest;
import org.example.tripservice.dto.TripResponseDTO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TripService {
    private final KafkaProducerService kafkaProducerService;
    private final RedisTemplate<String,TripRequest> restTemplate;

    public TripResponseDTO requestTrip(TripRequest request) {
        String tripId = UUID.randomUUID().toString();

        TripResponseDTO response =  TripResponseDTO.builder()
                .uuid(tripId)
                .errorCode(null)
                .errorKey(null)
                .plusOne(null)
                .riderHasCompletedTrips(true)  // Assuming true for now
                .build();

            restTemplate.opsForValue().set("KEY_TRIP "+tripId, request);
        kafkaProducerService.publishTripEvent(request,tripId);

        return  response;
    }
}
