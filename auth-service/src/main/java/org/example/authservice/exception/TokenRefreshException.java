package org.example.authservice.exception;

import java.io.Serializable;

public class TokenRefreshException extends RuntimeException {
    private String token;
    private String message;

    public TokenRefreshException(String token, String message) {
        super(String.format("Couldn't refresh token %s : %s", token, message));
        this.token = token;
        this.message = message;
    }
}
