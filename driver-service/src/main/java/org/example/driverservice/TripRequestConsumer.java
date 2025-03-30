package org.example.driverservice;

import lombok.extern.apachecommons.CommonsLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.protocol.types.Field;
import org.example.sharedlibs.TripDetails;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@CommonsLog(topic = "Consumer Logger")

public class TripRequestConsumer {

//    @KafkaListener(topics = "TRIP_REQUEST_EVENT", groupId = "driver-service-group" )
//    public void consumeTripRequestEvent(String tripRequest) {
//
//        log.info("Received trip request: " + tripRequest);
//
//    }
    @KafkaListener(topics = "TRIP_REQUEST_EVENT")
    public void consume(ConsumerRecord<String, TripDetails> record) {
        log.info(String.format("Consumed message -> %s", record.value()));
    }

}
