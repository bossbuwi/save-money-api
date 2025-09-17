package com.paradox.savemoney.web;

import com.paradox.savemoney.exception.EntityNotFoundException;
import com.paradox.savemoney.exception.UpstreamApiException;
import com.paradox.savemoney.util.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;

@Component
public class WebClientHelper {

    public boolean isError(HttpStatusCode status) {
        return status.is4xxClientError() || status.is5xxServerError();
    }

    public Mono<Throwable> handleError(ClientResponse response) {
        return response.bodyToMono(String.class)
                .flatMap(errorBody -> Mono.error(new UpstreamApiException(
                        response.statusCode(),
                        errorBody,
                        response.headers().asHttpHeaders()
                )));
    }

    public void processResponse(ResponseEntity<String> responseEntity,
                                SynchronousSink<ResponseEntity<String>> sink,
                                boolean checkEmpty) {
        String responseBody = responseEntity.getBody();

        if (checkEmpty && "[]".equals(responseBody)) {
            sink.error(new EntityNotFoundException("Item"));
            return;
        }

        String newBody = StringUtils.stripSurroundingBrackets(responseBody);
        HttpHeaders newHeaders = new HttpHeaders();
        newHeaders.set(HttpHeaders.CONTENT_TYPE, "application/json");
        newHeaders.set("Cache-Control", "no-cache");
        newHeaders.set("X-Content-Type-Options", "nosniff");
        ResponseEntity<String> newResponse = ResponseEntity
                .status(responseEntity.getStatusCode())
//                .headers(responseEntity.getHeaders())
                .headers(newHeaders)
                .body(newBody);
        sink.next(newResponse);
    }
}
