package org.example.locationservice.exception;

import lombok.Getter;

public class RouteNotFoundException extends RuntimeException {

    public RouteNotFoundException(String message) {
        super(message);
    }
}