package com.paradox.savemoney.exception;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static com.paradox.savemoney.web.HttpStructure.HeaderValue.APPLICATION_JSON;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler(UpstreamApiException.class)
    public ResponseEntity<String> handleUpstreamApiException(UpstreamApiException ex) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(CONTENT_TYPE, APPLICATION_JSON);
        return ResponseEntity.status(ex.getStatusCode())
                .headers(headers)
                .body(ex.getResponseBody());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
        ApiException apiException = new ApiException(HttpStatus.NOT_FOUND, ex.getMessage());
        return buildErrorResponse(apiException);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        ApiException apiException = new ApiException(HttpStatus.BAD_REQUEST, "Validation failed", errors);

        return buildErrorResponse(apiException);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();
        if (cause instanceof UnrecognizedPropertyException unrecognizedException) {
            String fieldName = unrecognizedException.getPropertyName();
            String errorMessage = "Unknown field: " + fieldName;
            return buildErrorResponse(new ApiException(HttpStatus.BAD_REQUEST, errorMessage));
        }

        // For other types of message not readable errors
        return buildErrorResponse(new ApiException(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<Object> handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
        ApiException apiException = new ApiException(HttpStatus.UNAUTHORIZED, ex.getMessage());
        return buildErrorResponse(apiException);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Object> handleUnauthorizedException(UnauthorizedException ex) {
        ApiException apiException = new ApiException(HttpStatus.UNAUTHORIZED, ex.getMessage());
        return buildErrorResponse(apiException);
    }

    private ResponseEntity<Object> buildErrorResponse(ApiException ex) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(CONTENT_TYPE, APPLICATION_JSON);
        return new ResponseEntity<>(ex, headers, ex.getHttpStatus());
    }
}
