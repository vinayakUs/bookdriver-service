package org.example.authservice.exception.advice;


import org.example.authservice.exception.*;
import org.example.authservice.model.dto.ApiResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@Slf4j
public class AuthControllerAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ApiResponseDto handleException(Exception ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return new ApiResponseDto(false, ex.getMessage(), ex.getClass().getName(), resolvePathFromWebRequest(request));
    }

    @ExceptionHandler(value = InvalidTokenRequestException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ResponseBody
    public ApiResponseDto handleInvalidTokenException(InvalidTokenRequestException ex, WebRequest request) {
        return new ApiResponseDto(false, ex.getMessage(), ex.getClass().getName(), resolvePathFromWebRequest(request));
    }

    @ExceptionHandler(PasswordResetException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ResponseBody
    public ApiResponseDto handlePasswordResetException (PasswordResetException ex, WebRequest request) {
        return new ApiResponseDto(false, ex.getMessage(), ex.getClass().getName(), resolvePathFromWebRequest(request));
    }


    private String resolvePathFromWebRequest(WebRequest request) {
        try {
            return ((ServletWebRequest)request).getRequest().getAttribute("javax.servlet.forward.request_uri").toString();
        }catch (Exception e) {
return null;
        }
    }

    @ExceptionHandler(value = MailSendException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ResponseBody
    public ApiResponseDto handleMailSendException(MailSendException ex, WebRequest request) {
        return new ApiResponseDto(false, ex.getMessage(), ex.getClass().getName(), resolvePathFromWebRequest(request));
    }


    @ExceptionHandler(value = ResourceAlreadyInUseException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ApiResponseDto handleResourceAlreadyInUseException(ResourceAlreadyInUseException ex, WebRequest request) {
        return new ApiResponseDto(false, ex.getMessage(), ex.getClass().getName(), resolvePathFromWebRequest(request));
    }

    @ExceptionHandler(value = PasswordResetLinkException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ResponseBody
    public ApiResponseDto handlePasswordResetLinkException(PasswordResetLinkException ex, WebRequest request){
        return new ApiResponseDto(false, ex.getMessage(), ex.getClass().getName(), resolvePathFromWebRequest(request));

    }
   

    @ExceptionHandler(value = ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiResponseDto handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        return new ApiResponseDto(false, ex.getMessage(), ex.getClass().getName(), resolvePathFromWebRequest(request));
    }

    @ExceptionHandler(value = UserRegistrationException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ResponseBody
    public ApiResponseDto handleUserRegistrationException(UserRegistrationException ex, WebRequest request) {
        return new ApiResponseDto(false, ex.getMessage(), ex.getClass().getName(), resolvePathFromWebRequest(request));
    }

    @ExceptionHandler(value = UserLoginException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ResponseBody
    public ApiResponseDto handleUserLoginException(UserLoginException ex, WebRequest request) {
        return new ApiResponseDto(false, ex.getMessage(), ex.getClass().getName(), resolvePathFromWebRequest(request));
    }

    @ExceptionHandler(TokenRefreshException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResponseDto handleTokenRefreshException(TokenRefreshException ex,WebRequest request){
        return new ApiResponseDto(false, ex.getMessage(), ex.getClass().getName(), resolvePathFromWebRequest(request));
    }

}
