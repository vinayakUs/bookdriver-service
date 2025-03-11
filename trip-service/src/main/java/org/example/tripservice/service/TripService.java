package org.example.tripservice.service;

import lombok.RequiredArgsConstructor;
import org.example.tripservice.TripRequest;
import org.example.tripservice.dto.TripResponseDTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TripService {
    private final KafkaProducerService kafkaProducerService;

    public TripResponseDTO requestTrip(TripRequest request) {
        String tripId = UUID.randomUUID().toString();

        TripResponseDTO response =  TripResponseDTO.builder()
                .uuid(tripId)
                .errorCode(null)
                .errorKey(null)
                .plusOne(null)
                .riderHasCompletedTrips(true)  // Assuming true for now
                .build();

        kafkaProducerService.publishTripEvent(request,tripId);
        return  response;
    }
}
