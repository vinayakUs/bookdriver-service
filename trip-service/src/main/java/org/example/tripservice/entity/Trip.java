package org.example.tripservice.entity;
import java.time.Instant;
import java.time.LocalDateTime;

import org.example.tripservice.enums.TripStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;



@Entity
@Table(name = "trips")
public class Trip {


    @Id
    @Column(name = "tripId")
    private String tripId;

    @Column(name = "userId")
    private String userId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TripStatus status; // REQUESTED, ACCEPTED, IN_PROGRESS, COMPLETED, CANCELLED
    
    @Column(name = "ts")
    private Instant timestamp;

    @Column(name = "source")
    private String source;

    @Column(name = "destination")
    private String destination;



}

