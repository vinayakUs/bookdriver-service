package org.example.tripservice.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.example.sharedlibs.avro.Location;

import java.io.Serializable;

@Data
@Setter
@Getter
public class Origin implements Serializable {
    Location location;




    // Getters and setters
}