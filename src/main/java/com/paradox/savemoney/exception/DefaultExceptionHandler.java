package com.paradox.savemoney.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static com.paradox.savemoney.config.structure.HttpStructure.HeaderValue.APPLICATION_JSON;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class DefaultExceptionHandler extends ResponseEntityExceptionHandler {

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

    private ResponseEntity<Object> buildErrorResponse(ApiException ex) {
        return new ResponseEntity<>(ex, ex.getHttpStatus());
    }
}
