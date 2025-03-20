package org.example.locationservice.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigInteger;

@Data

public class RouteResponseDto {
    private String polyline;
    private BigInteger distance;
    private BigInteger eta;
}
