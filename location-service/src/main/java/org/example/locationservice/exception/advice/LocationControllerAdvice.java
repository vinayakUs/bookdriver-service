package org.example.locationservice.exception.advice;

import lombok.extern.slf4j.Slf4j;
import org.example.locationservice.exception.PlaceNotFoundException;
import org.example.locationservice.exception.RouteNotFoundException;
import org.example.locationservice.exception.RouteServiceException;
import org.example.locationservice.model.dto.ApiResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;

@RestControllerAdvice
@Slf4j
public class LocationControllerAdvice {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ApiResponseDto<String> handleException(Exception ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return new ApiResponseDto<>(false, ex.getMessage(), ex.getClass().getName(), resolvePathFromWebRequest(request));
    }

    private String resolvePathFromWebRequest(WebRequest request) {
        try {
            return ((ServletWebRequest) request).getRequest().getAttribute("javax.servlet.forward.request_uri").toString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Handle MethodArgumentNotValidException for @Valid
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponseDto<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        HashMap<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            errors.put(error.getObjectName(), error.getDefaultMessage());
        });
        return new ApiResponseDto<>(false, "Validation Error", ex.getClass().getName(), resolvePathFromWebRequest(request));
    }


    @ExceptionHandler(PlaceNotFoundException.class)
    public ResponseEntity<ApiResponseDto> handlePlaceNotFoundException(PlaceNotFoundException ex, WebRequest request) {
        return new ResponseEntity<ApiResponseDto>(new ApiResponseDto(false,ex.getMessage()), HttpStatusCode.valueOf(ex.getErrorCode().getCode()));
    }

    /*
    // Route Internal Exception not found
     */
    @ExceptionHandler(RouteServiceException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponseDto handleRouteException(RouteServiceException ex, WebRequest request) {
        log.info(ex.getMessage(), ex);
        return new ApiResponseDto(false,ex.getMessage());

    }

    /*
      // Route Internal Exception not found
       */
    @ExceptionHandler(RouteNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponseDto handleRouteException(RouteNotFoundException ex, WebRequest request) {
        log.info(ex.getMessage(), ex);
        return new ApiResponseDto(false,ex.getMessage());

    }
}
