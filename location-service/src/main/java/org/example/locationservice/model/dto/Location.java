package org.example.locationservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public  class Location {
    private double lat;
    private double lng;
    private TSType type;
}