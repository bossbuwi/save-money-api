package com.paradox.savemoney.exception;

import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

@Getter
public class UpstreamApiException extends ResponseStatusException {
    private final HttpStatusCode statusCode;
    private final String responseBody;
    private final HttpHeaders headers;

    public UpstreamApiException(HttpStatusCode statusCode, String responseBody, HttpHeaders headers) {
        super(statusCode, responseBody);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
        this.headers = headers;
    }
}
