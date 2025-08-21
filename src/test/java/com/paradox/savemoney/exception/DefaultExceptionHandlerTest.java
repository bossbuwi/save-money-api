package com.paradox.savemoney.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.paradox.savemoney.config.structure.HttpStructure.HeaderValue.APPLICATION_JSON;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultExceptionHandlerTest {
    private DefaultExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new DefaultExceptionHandler();
    }

    @Test
    void handleUpstreamApiException_ReturnsResponseFromException() {
        String responseBody = "{\"error\":\"Not Found\"}";
        UpstreamApiException ex = new UpstreamApiException(
                HttpStatus.NOT_FOUND,
                responseBody,
                null
        );

        ResponseEntity<String> response = exceptionHandler.handleUpstreamApiException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(responseBody, response.getBody());
        assertEquals(APPLICATION_JSON, response.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE));
    }

    @Test
    void handleEntityNotFound_ReturnsResponseFromException() {
        EntityNotFoundException ex = new EntityNotFoundException("Item");

        ResponseEntity<Object> response = exceptionHandler.handleEntityNotFound(ex);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ApiException body = (ApiException) response.getBody();
        assertTrue(Objects.requireNonNull(body).getMessage().contains("Item"));
    }


    @Test
    void handleMethodArgumentNotValid_ReturnsValidationErrors() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        FieldError fieldError1 = new FieldError("object", "field1", "must not be null");
        FieldError fieldError2 = new FieldError("object", "field2", "must be positive");
        List<FieldError> fieldErrors = Arrays.asList(fieldError1, fieldError2);

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        HttpHeaders headers = new HttpHeaders();
        WebRequest request = mock(WebRequest.class);

        ResponseEntity<Object> response = exceptionHandler.handleMethodArgumentNotValid(
                ex, headers, HttpStatus.BAD_REQUEST, request
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(APPLICATION_JSON, response.getHeaders().getFirst("Content-Type"));

        ApiException body = (ApiException) response.getBody();
        assertNotNull(body);
        assertEquals(HttpStatus.BAD_REQUEST, body.getHttpStatus());
        assertEquals("Validation failed", body.getMessage());

        Map<String, String> errors = (Map<String, String>) body.getErrors();
        assertEquals("must not be null", errors.get("field1"));
        assertEquals("must be positive", errors.get("field2"));
    }
}
