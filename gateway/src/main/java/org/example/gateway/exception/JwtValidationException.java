package org.example.gateway.exception;

public class JwtValidationException extends  RuntimeException{
    String message;
    Throwable cause;
    public JwtValidationException(String message, Throwable cause) {
        super(message);
        this.message = message;
        this.cause = cause;
    }
}
