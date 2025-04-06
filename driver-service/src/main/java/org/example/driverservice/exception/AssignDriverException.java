package org.example.driverservice.exception;


public class AssignDriverException extends RuntimeException {
    String message;
    public AssignDriverException(String message) {
        super("Driver Assignment Error" + message);
        this.message=message;
    }
}
