package com.codefolio.handler;

import com.codefolio.dto.error.ApiErrorResponse;
import com.codefolio.exception.exceptions.ResourceNotFoundException;
import com.codefolio.exception.exceptions.CredentialsAlreadyInUseException;
import com.codefolio.exception.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

/**
 * API Exception handler class
 */
@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {ResourceNotFoundException.class, UsernameNotFoundException.class, UserNotFoundException.class})
    public ResponseEntity<ApiErrorResponse> resourceNotFoundExceptionHandler(RuntimeException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return buildResponse(request, status, ex.getMessage());
    }

    @ExceptionHandler(value = CredentialsAlreadyInUseException.class)
    public ResponseEntity<ApiErrorResponse> conflictExceptionHandler(RuntimeException ex, WebRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        return buildResponse(request, status, ex.getMessage());
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(WebRequest request, HttpStatus status, String message) {
        String uri = ((ServletWebRequest) request).getRequest().getRequestURI();
        ApiErrorResponse apiErrorDto = new ApiErrorResponse(status, message, uri);

        return ResponseEntity.status(status).body(apiErrorDto);
    }

}
