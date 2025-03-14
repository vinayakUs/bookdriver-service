package org.example.driverservice;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TripRequestConsumer {

    @KafkaListener(topics = "TRIP_REQUEST_EVENT", groupId = "driver-service-group" )
public void consumeTripRequestEvent(TripRequest tripRequest) {
log.info("Received trip request: " + tripRequest);
}

}
