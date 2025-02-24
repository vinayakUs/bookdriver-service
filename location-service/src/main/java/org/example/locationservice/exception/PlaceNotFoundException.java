package org.example.locationservice.exception;

public class PlaceNotFoundException extends RuntimeException {

    private String placeId;

    public PlaceNotFoundException(String message, String placeId) {
        super(String.format("Place with id[%s] not found", placeId));
        this.placeId=placeId;
    }

}