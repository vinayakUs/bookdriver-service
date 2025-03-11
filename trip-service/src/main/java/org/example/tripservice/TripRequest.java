package org.example.tripservice;


import lombok.Data;

import java.util.List;

@Data
public class TripRequest{
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
class Coordinate {
    private double latitude;
    private double longitude;

    // Getters and setters
}

@Data
class Location {
    private String addressLine1;
    private String addressLine2;
    private Coordinate coordinate;
    private String id;
    private String provider;
    private String source;

    // Getters and setters
}

@Data
class Origin {
    private Location location;

    // Getters and setters
}
@Data
class Payment {
    private String paymentMethodDisplayName;
    private String profileUUID;

    // Getters and setters
}
//@Data
//  class VehicleView {
//    private String description;
//    private int id;
//
// }
