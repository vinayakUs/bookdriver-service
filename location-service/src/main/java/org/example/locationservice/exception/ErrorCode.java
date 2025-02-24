package org.example.locationservice.exception;

public enum ErrorCode {
    INVALID_REQUEST(400, "Invalid request"),
    NOT_FOUND(404, "Not found"),
    INTERNAL_SERVER_ERROR(500, "Internal server error"),
    NETWORK_ERROR(503, "Network error"),
    UNAUTHORIZED(401, "Unauthorized access");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}