package org.example.locationservice.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RouteRequestDto {
    @NotNull
    @NotEmpty
    private Location pickupLocation;

    @NotNull
    @NotEmpty
    private Location destinationLocation;

}
