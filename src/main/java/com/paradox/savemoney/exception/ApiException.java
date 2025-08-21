package com.paradox.savemoney.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
public class ApiException {
    @JsonIgnore
    private HttpStatus httpStatus;
    private String status;
    private String message;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss.s")
    private final LocalDateTime timestamp;
    private Map<String, String> errors;

    private ApiException() {
        this.timestamp = LocalDateTime.now();
    }

    public ApiException(HttpStatus httpStatus) {
        this();
        this.httpStatus = httpStatus;
        this.status = httpStatus.getReasonPhrase();
    }

    public ApiException(HttpStatus httpStatus, String message) {
        this();
        this.httpStatus = httpStatus;
        this.status = httpStatus.getReasonPhrase();
        this.message = message;
    }

    public ApiException(HttpStatus httpStatus, String message, Map<String, String> errors) {
        this();
        this.httpStatus = httpStatus;
        this.status = httpStatus.getReasonPhrase();
        this.message = message;
        this.errors = errors;
    }
}
