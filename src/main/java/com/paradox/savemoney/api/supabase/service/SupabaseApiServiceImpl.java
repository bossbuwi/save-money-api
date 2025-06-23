package com.paradox.savemoney.api.supabase.service;

import com.paradox.savemoney.api.supabase.model.CreateItemRequest;
import com.paradox.savemoney.api.supabase.model.ItemBase;
import com.paradox.savemoney.api.supabase.model.UpdateItemRequest;
import com.paradox.savemoney.exception.EntityNotFoundException;
import com.paradox.savemoney.exception.UpstreamApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.paradox.savemoney.api.supabase.structure.SupabaseStructure.HeaderKey.APIKEY;
import static com.paradox.savemoney.api.supabase.structure.SupabaseStructure.HeaderKey.PREFER;
import static com.paradox.savemoney.api.supabase.structure.SupabaseStructure.HeaderValue.REPRESENTATION;
import static com.paradox.savemoney.config.structure.HttpStructure.HeaderValue.APPLICATION_JSON;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@Service
public class SupabaseApiServiceImpl implements SupabaseApiService {
    private final WebClient webClient;
    @Value("${api.supabase.key}")
    private String authHeaderToken;

    public SupabaseApiServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<ResponseEntity<String>> getAllItems() {
        final String path = "/rest/v1/items?select=*";
        return webClient.get()
                .uri(path)
                .headers(headers -> {
                    headers.add(AUTHORIZATION, authHeaderToken);
                    headers.add(APIKEY, authHeaderToken);
                })
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new UpstreamApiException(
                                        response.statusCode(),
                                        errorBody,
                                        response.headers().asHttpHeaders()
                                )))
                )
                .toEntity(String.class);
    }

    @Override
    public Mono<ResponseEntity<String>> getItem(String id) {
        final String path = "/rest/v1/items?_id=eq." + id;
        return webClient.get()
                .uri(path)
                .headers(headers -> {
                    headers.add(AUTHORIZATION, authHeaderToken);
                    headers.add(APIKEY, authHeaderToken);
                })
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new UpstreamApiException(
                                        response.statusCode(),
                                        errorBody,
                                        response.headers().asHttpHeaders()
                                )))
                )
                .toEntity(String.class)
                .flatMap(responseEntity -> {
                    String emptyBody = "[]";
                    String body = responseEntity.getBody();

                    if (emptyBody.equalsIgnoreCase(body)) {
                        return Mono.error(new EntityNotFoundException("Item"));
                    }
                    return Mono.just(responseEntity);
                });
    }

    @Override
    public Mono<ResponseEntity<String>> addItem(CreateItemRequest request) {
        final String path = "/rest/v1/items";
        return webClient.post()
                .uri(path)
                .headers(headers -> {
                    headers.add(AUTHORIZATION, authHeaderToken);
                    headers.add(APIKEY, authHeaderToken);
                    headers.add(CONTENT_TYPE, APPLICATION_JSON);
                    headers.add(PREFER, REPRESENTATION);
                })
                .bodyValue(request)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new UpstreamApiException(
                                        response.statusCode(),
                                        errorBody,
                                        response.headers().asHttpHeaders()
                                )))
                )
                .toEntity(String.class);
        // TODO: Needs to strip the response body of the []
    }

    @Override
    public Mono<ResponseEntity<String>> editItem(UpdateItemRequest request) {
        return null;
    }

    @Override
    public Mono<ResponseEntity<String>> editItemById(String id, CreateItemRequest request) {
        return null;
    }

    @Override
    public Mono<String> deleteItem(long id) {
        return null;
    }
}
