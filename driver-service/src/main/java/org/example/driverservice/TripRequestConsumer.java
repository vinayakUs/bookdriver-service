package org.example.driverservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field;
import org.example.driverservice.service.AssignmentService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TripRequestConsumer {
    private final AssignmentService assignmentService;

    @KafkaListener(topics = "TRIP_REQUEST_EVENT", groupId = "driver-service-group" )
public void consumeTripRequestEvent(org.example.sharedlibs.TripDetails tripDetails) {
log.info("Received trip request: " + tripDetails);
assignmentService.assignDriver(tripDetails);

}

}
