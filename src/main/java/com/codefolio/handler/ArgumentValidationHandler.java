package com.codefolio.handler;

import com.codefolio.dto.error.ApiErrorResponse;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Handler for request body validation errors.
 */
@ControllerAdvice
public class ArgumentValidationHandler extends ResponseEntityExceptionHandler {

    /**
     * Returns Bad Request with the invalid field's name and the error.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatusCode status,
                                                                  @NonNull WebRequest request) {

        // collect all invalid fields and the given validation message for it
        Map<String, String> errors = ex.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    String fieldName = ((FieldError) error).getField();
                    // message could be null so to avoid a NPE we return empty string instead
                    String message = error.getDefaultMessage() == null ? "" : error.getDefaultMessage();
                    return Map.of(fieldName, message);
                })
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        String uri = ((ServletWebRequest) request).getRequest().getRequestURI();
        ApiErrorResponse errorResponse = new ApiErrorResponse(status, errors, uri);
        return ResponseEntity.status(status).body(errorResponse);
    }

}
