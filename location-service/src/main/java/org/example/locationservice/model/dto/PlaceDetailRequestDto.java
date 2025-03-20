package org.example.locationservice.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class PlaceDetailRequestDto {

    @NotNull
    @NotBlank
    private String placeId;
    @NotBlank
    @NotNull
    private String sessionToken;

}
