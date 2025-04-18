package org.example.driverservice.exception;

public class NoDriversAvailableException extends RuntimeException {
    String message;
    Throwable throwable;
    public NoDriversAvailableException(String message,Throwable throwable) {
        super(message);
        this.message = message;
        this.throwable = throwable;
    }

    public NoDriversAvailableException(String message ) {
        super(message);
        this.message = message;
        this.throwable = null;
    }
}
