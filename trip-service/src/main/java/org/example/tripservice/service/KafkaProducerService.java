package org.example.tripservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tripservice.TripRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.event.trip.requested}")
    private String tripRequestedTopic;

    public void publishTripEvent(TripRequest trip, String tripId) {
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(tripRequestedTopic, tripId, trip);
        future.whenComplete((r, e) -> {
            if(e != null) {
                log.info("Error sending trip event"+ e);
            }else {
                log.info("Success sending trip event");
            }
        });
    }
}
