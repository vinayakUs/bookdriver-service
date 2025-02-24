package org.example.locationservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class PlaceDetailRequestDto {

    private String placeId;
        private String sessionToken;

}
