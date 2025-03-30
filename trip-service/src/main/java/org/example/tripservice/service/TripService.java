package org.example.tripservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.*;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.example.sharedlibs.TRIP_STATUS;
import org.example.sharedlibs.Test123;
import org.example.sharedlibs.TripDetails;
import org.example.tripservice.AvroToJsonConverter;
import org.example.tripservice.dto.TripRequest;
import org.example.tripservice.dto.TripResponseDTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TripService {

    private final KafkaProducerService kafkaProducerService;
    private final RedisService redisService;
    private final AvroToJsonConverter avroToJsonConverter;




    public TripResponseDTO requestTrip(TripRequest request)   {
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
        trip.setTripStatus(TRIP_STATUS.TRIP_REQUESTED);


        try {
            redisService.storeTrip("TRIP_REQUEST:"+tripId,avroToJsonConverter.deserialize(trip));
            kafkaProducerService.publishTripEvent(tripId,trip);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return  response;
    }

}
