package com.paradox.savemoney.api.auth.service;

import com.paradox.savemoney.api.auth.model.EmailAuthRequest;
import com.paradox.savemoney.web.WebClientHelper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AuthServiceImpl implements AuthService {
    private final WebClient webClient;
    private final WebClientHelper webClientHelper;

    public AuthServiceImpl(@Qualifier("authWebClient") WebClient webClient,
                           WebClientHelper webClientHelper) {
        this.webClient = webClient;
        this.webClientHelper = webClientHelper;
    }

    @Override
    public Mono<ResponseEntity<String>> login(EmailAuthRequest request) {
        final String path = "/auth/v1/token?grant_type=password";
        return webClient.post()
                .uri(path)
                .bodyValue(request)
                .retrieve()
                .onStatus(webClientHelper::isError, webClientHelper::handleError)
                .toEntity(String.class)
                .handle((responseEntity, sink) ->
                        webClientHelper.processResponse(responseEntity, sink, false));
    }
}
