package org.example.locationservice.exception;

import lombok.Getter;

@Getter
public class PlaceNotFoundException extends RuntimeException {

     String placeId;
     ErrorCode errorCode;

    public PlaceNotFoundException(String message, String placeId, ErrorCode errorCode) {
        super(String.format("Place Id Not Found [%s]: %s", placeId,message));
        this.placeId=placeId;
        this.errorCode=errorCode;
    }

}