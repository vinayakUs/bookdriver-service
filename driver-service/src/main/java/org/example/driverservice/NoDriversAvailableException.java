package org.example.driverservice;

public class NoDriversAvailableException extends RuntimeException {
    String message;
    public NoDriversAvailableException(String message) {
        super("No drivers available");
        this.message = message;
    }
}
