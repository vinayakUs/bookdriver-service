package org.example.driverservice.exception;


public class AssignDriverException extends RuntimeException {
    String message;
    String error;
    public AssignDriverException(String message) {
        super(message);
        this.message=message;
    }
    public AssignDriverException(String message,String error) {
        super(message);
        this.message=message;
        this.error=error;
    }
}
