package org.example.tripservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.sharedlibs.TRIP_STATUS;
import org.example.sharedlibs.TripDetails;
import org.example.tripservice.AvroToJsonConverter;
import org.example.tripservice.dto.TripRequest;
import org.example.tripservice.dto.TripResponseDTO;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TripService {

    private final KafkaProducerService kafkaProducerService;
    private final RedisService redisService;
    private final AvroToJsonConverter avroToJsonConverter;


    public TripResponseDTO requestTrip(TripRequest request) {
        String tripId = UUID.randomUUID().toString();

        TripResponseDTO response = TripResponseDTO.builder()
                .uuid(tripId)
                .errorCode(null)
                .errorKey(null)
                .plusOne(null)
                .riderHasCompletedTrips(true)
                .build();

        TripDetails trip = new TripDetails();
        trip.setTripId(tripId);
        trip.setDestination(request.getDestinations().get(0));
        trip.setSource(request.getOrigin().getLocation());
        trip.setTripStatus(TRIP_STATUS.TRIP_REQUESTED);

        try {
            redisService.storeTrip("TRIP_REQUEST:" + tripId, avroToJsonConverter.deserialize(trip));
            kafkaProducerService.publishTripEvent(tripId, trip);
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setErrorCode("500");
            return response;
        }
        return response;
    }

}
