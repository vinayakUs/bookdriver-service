package org.example.driverservice.service;

import com.redis.lettucemod.api.StatefulRedisModulesConnection;
import io.lettuce.core.GeoWithin;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.example.driverservice.exception.AssignDriverException;
import org.example.driverservice.exception.NoDriversAvailableException;
import org.example.sharedlibs.avro.TripDetails;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class AssignmentService {
    public static final String LOCK_USER = "LOCK:USER";
    public static final String LOCK_DRIVER = "LOCK:DRIVER";
    private final GenericObjectPool<StatefulRedisModulesConnection<String, String>> pool;
    private final DriverService driverService;
    private final RedissonClient redissonClient;
    private final HttpServletRequest httpServletRequest;

    private void processDriverAssignment(TripDetails tripDetails, String driverId) {
        RLock driverRLock = redissonClient.getLock("LOCK:DRIVER" + driverId);
    }

    public void assignDriver(TripDetails tripDetails) {

        // Phase 1: Reserve driver
        GeoWithin<String> driver = driverService.findAndReserveDriver(tripDetails.getSource(), 50)
                .orElseThrow(() -> new NoDriversAvailableException(
                        String.format("No driver available for trip (%f,%f)",
                                tripDetails.getSource().getCoordinate().getLongitude(),
                                tripDetails.getSource().getCoordinate().getLatitude())
                ));


        boolean assignmentSuccess = false;


        // Phase 2: Acquire user lock
        RLock userLock = null;
        RLock driverLock = null;

        try {
            String userId = tripDetails.getUserId().toString();
            userLock = redissonClient.getLock("USER:" + userId);
            if (!userLock.tryLock(500, 30, TimeUnit.SECONDS)) {
                throw new AssignDriverException("Could not acquire user lock");
            }

            // Phase 3: Finalize with driver lock
            driverLock = redissonClient.getLock("DRIVER:" + driver.getMember());
            if (!driverLock.tryLock(500, 30, TimeUnit.SECONDS)) {
                throw new AssignDriverException("Could not acquire driver lock");
            }
            completeAssignment(tripDetails, driver.getMember());

            assignmentSuccess = true;


        } catch (Exception e) {
            driverService.releaseDriver(driver.getMember());
            throw new AssignDriverException("Assignment failed", e);
        } finally {
            unlockSafely(userLock);
            unlockSafely(driverLock);

            // Release driver only if assignment failed
            if (!assignmentSuccess && driver != null) {
                driverService.releaseDriver(driver.getMember());
            }
        }


    }

    private void completeAssignment(TripDetails tripDetails, String member) {

        log.info("Complete Assignment for trip {} and driver {}", tripDetails.getTripId(), member);

    }

    private void unlockSafely(RLock lock) {
        if (lock != null && lock.isHeldByCurrentThread()) {
            try {
                lock.unlock();
            } catch (IllegalMonitorStateException e) {
                log.warn("Lock already released: {}", lock.getName());
            }
        }
    }
}



