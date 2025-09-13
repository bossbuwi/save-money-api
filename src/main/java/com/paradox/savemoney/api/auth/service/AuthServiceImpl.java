package com.paradox.savemoney.api.auth.service;

import com.paradox.savemoney.api.auth.model.EmailAuthRequest;
import com.paradox.savemoney.exception.EntityNotFoundException;
import com.paradox.savemoney.exception.UpstreamApiException;
import com.paradox.savemoney.util.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;

@Service
public class AuthServiceImpl implements AuthService {
    private final WebClient webClient;

    public AuthServiceImpl(@Qualifier("authWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<ResponseEntity<String>> login(EmailAuthRequest request) {
        final String path = "/auth/v1/token?grant_type=password";
        return webClient.post()
                .uri(path)
                .bodyValue(request)
                .retrieve()
                .onStatus(this::isError, this::handleError)
                .toEntity(String.class)
                .handle((responseEntity, sink) ->
                        processResponse(responseEntity, sink, false));
    }

    private boolean isError(HttpStatusCode status) {
        return status.is4xxClientError() || status.is5xxServerError();
    }

    private Mono<Throwable> handleError(ClientResponse response) {
        return response.bodyToMono(String.class)
                .flatMap(errorBody -> Mono.error(new UpstreamApiException(
                        response.statusCode(),
                        errorBody,
                        response.headers().asHttpHeaders()
                )));
    }

    private void processResponse(ResponseEntity<String> responseEntity,
                                 SynchronousSink<ResponseEntity<String>> sink,
                                 boolean checkEmpty) {
        String responseBody = responseEntity.getBody();

        if (checkEmpty && "[]".equals(responseEntity.getBody())) {
            sink.error(new EntityNotFoundException("Item"));
            return;
        }

        String newBody = StringUtils.stripSurroundingBrackets(responseBody);

        ResponseEntity<String> newResponse = new ResponseEntity<>(
                newBody,
                responseEntity.getHeaders(),
                responseEntity.getStatusCode());
        sink.next(newResponse);
    }
}
