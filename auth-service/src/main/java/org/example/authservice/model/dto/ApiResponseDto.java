package org.example.authservice.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseDto {

    private final String data;
    private final Boolean success;
    private final String timestamp;
    private final String cause;
    private final String path;

    public ApiResponseDto(Boolean success, String data, String cause, String path) {
        this.timestamp = Instant.now().toString();
        this.data = data;
        this.success = success;
        this.cause = cause;
        this.path = path;
    }

    public ApiResponseDto(Boolean success, String data) {
        this.timestamp = Instant.now().toString();
        this.data = data;
        this.success = success;
        this.cause = null;
        this.path = null;
    }

    public String getData() {
        return data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getCause() {
        return cause;
    }

    public String getPath() {
        return path;
    }
}
