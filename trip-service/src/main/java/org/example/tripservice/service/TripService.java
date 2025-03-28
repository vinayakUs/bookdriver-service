package org.example.tripservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.sharedlibs.TripDetails;
import org.example.tripservice.dto.TRIP_STAUS;
import org.example.tripservice.dto.TripRequest;
import org.example.tripservice.dto.TripResponseDTO;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TripService {
    private final KafkaProducerService kafkaProducerService;

    private final RedisService redisService;

//    private GenericObjectPool<StatefulRedisModulesConnection<String, String>> pool;



    public TripResponseDTO requestTrip(TripRequest request)  {
        String tripId = UUID.randomUUID().toString();

        TripResponseDTO response =  TripResponseDTO.builder()
                .uuid(tripId)
                .errorCode(null)
                .errorKey(null)
                .plusOne(null)
                .riderHasCompletedTrips(true)  // Assuming true for now
                .build();



        ObjectMapper objectMapper = new ObjectMapper();
        TripDetails trip  = new TripDetails();
        trip.setTripId(tripId);
        trip.setDestination(request.getDestinations().get(0));
        trip.setSource(request.getOrigin().getLocation());
        trip.setTripStatus(TRIP_STAUS.TRIP_REQUESTED);


        try {
            redisService.storeTrip("TRIP_REQUEST:"+tripId,objectMapper.writeValueAsString(trip));
            kafkaProducerService.publishTripEvent(trip,tripId);

        }catch (Exception e){
            System.out.println(e.getMessage());

        }

        return  response;
    }

}
