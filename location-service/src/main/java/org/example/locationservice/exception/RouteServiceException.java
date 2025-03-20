package org.example.locationservice.exception;

import lombok.Getter;

public class RouteServiceException extends RuntimeException {

    public RouteServiceException(String message) {
        super(message);
    }
}
