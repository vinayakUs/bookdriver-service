package org.example.driverservice.exception;


public class AssignDriverException extends RuntimeException {
    String message;
    Throwable throwable;

    public AssignDriverException(String message,Throwable throwable) {
        super(message);
        this.message=message;
        this.throwable=throwable;
    }

    public AssignDriverException(String message ) {
        super(message);
        this.message=message;
        this.throwable=null;
    }
}
