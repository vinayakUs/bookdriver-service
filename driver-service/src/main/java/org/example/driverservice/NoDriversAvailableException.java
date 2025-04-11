package org.example.driverservice;

public class NoDriversAvailableException extends RuntimeException {
    String message;
    public NoDriversAvailableException(String message) {
        super(message);
        this.message = message;
    }
}
