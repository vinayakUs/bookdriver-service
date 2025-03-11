package org.example.tripservice.dto;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TripResponseDTO {
    private String errorCode;
    private String errorKey;
    private Object plusOne; // Can be another class if needed
    private Boolean riderHasCompletedTrips;
    private String uuid;
    private Object checkoutActionParameters; // Can be another class if needed
}
