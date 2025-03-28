package org.example.tripservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TripDetails {
    public  String tripId;
    public Location source;
    public Location destination;
    public TRIP_STAUS tripStatus;
}
