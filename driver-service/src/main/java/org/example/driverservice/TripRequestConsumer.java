package org.example.driverservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.driverservice.exception.AssignDriverException;
import org.example.driverservice.service.AssignmentService;
import org.example.sharedlibs.avro.TripDetails;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TripRequestConsumer {
    private final AssignmentService assignmentService;

    /**
     * @param tripDetails
     * sequenceDiagram
     *     participant User
     *     participant Service
     *     participant Redis
     *
     *     User->>Service: Request Trip
     *     Service->>Redis: 1. Find nearby drivers (no lock)
     *     Service->>Redis: 2. Reserve driver (short lock)
     *     Service->>Redis: 3. Get user lock (long lock)
     *     Service->>Redis: 4. Finalize assignment
     */

    @KafkaListener(topics = "TRIP_REQUEST_EVENT", groupId = "driver-service-group")
    public void consumeTripRequestEvent(TripDetails tripDetails) {

        try {
            log.info("Processing trip {}", tripDetails.getTripId());
            assignmentService.assignDriver(tripDetails);
        } catch (NoDriversAvailableException e) {
            handleDriverUnavailable(tripDetails, e);
        } catch (AssignDriverException e) {
            handleTechnicalFailure(tripDetails, e);
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private void handleTechnicalFailure(TripDetails tripDetails, AssignDriverException e) {
    }

    private void handleDriverUnavailable(TripDetails tripDetails, NoDriversAvailableException e) {
    }

}
