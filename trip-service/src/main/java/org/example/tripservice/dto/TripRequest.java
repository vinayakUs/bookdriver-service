package org.example.tripservice.dto;


import lombok.Data;
import org.example.sharedlibs.avro.Location;

import java.io.Serializable;
import java.util.List;

@Data
public class TripRequest implements Serializable {
    //    private Attribution attribution;
    private int capacity;
    private List<Location> destinations;
    private String locationSource;
    //    private String meta;
    private Origin origin;
//    private Payment payment;
//    private String type;
//    private VehicleView vehicleView;

}
//@Data
//  class Attribution {
//    private String sourceURL;
//
//}

@Data
class Coordinate implements Serializable {
    private double latitude;
    private double longitude;

    // Getters and setters
}



@Data
class Payment implements Serializable {
     String paymentMethodDisplayName;
     String profileUUID;

    // Getters and setters
}
//@Data
//  class VehicleView {
//    private String description;
//    private int id;
//
// }
