package com.paradox.savemoney.api.supabase.service;

import com.paradox.savemoney.api.supabase.model.CreateItemRequest;
import com.paradox.savemoney.api.supabase.model.UpdateItemRequest;
import com.paradox.savemoney.exception.EntityNotFoundException;
import com.paradox.savemoney.exception.UpstreamApiException;
import com.paradox.savemoney.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.paradox.savemoney.api.supabase.structure.SupabaseStructure.HeaderKey.APIKEY;
import static com.paradox.savemoney.api.supabase.structure.SupabaseStructure.HeaderKey.PREFER;
import static com.paradox.savemoney.api.supabase.structure.SupabaseStructure.HeaderValue.RESOLUTION_MERGE_DUPLICATES;
import static com.paradox.savemoney.api.supabase.structure.SupabaseStructure.HeaderValue.RETURN_REPRESENTATION;
import static com.paradox.savemoney.config.structure.HttpStructure.HeaderValue.APPLICATION_JSON;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@Service
public class SupabaseApiServiceImpl implements SupabaseApiService {
    @Value("${api.supabase.key}")
    private String authHeaderToken;
    private final WebClient webClient;
    private final StringUtils stringUtils;

    public SupabaseApiServiceImpl(WebClient webClient, StringUtils stringUtils) {
        this.webClient = webClient;
        this.stringUtils = stringUtils;
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
                .handle((responseEntity, sink) -> {
                    if ("[]".equals(responseEntity.getBody())) {
                        sink.error(new EntityNotFoundException("Item"));
                        return;
                    }
                    String responseBody = responseEntity.getBody();
                    String newBody = stringUtils.stripSurroundingBrackets(responseBody);
                    ResponseEntity<String> newResponse = new ResponseEntity<>(
                            newBody,
                            responseEntity.getHeaders(),
                            responseEntity.getStatusCode());
                    sink.next(newResponse);
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
                    headers.add(PREFER, RETURN_REPRESENTATION);
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
                .toEntity(String.class)
                .handle((responseEntity, sink) -> {
                    String responseBody = responseEntity.getBody();
                    String newBody = stringUtils.stripSurroundingBrackets(responseBody);
                    ResponseEntity<String> newResponse = new ResponseEntity<>(
                            newBody,
                            responseEntity.getHeaders(),
                            responseEntity.getStatusCode());
                    sink.next(newResponse);
                });
    }

    @Override
    public Mono<ResponseEntity<String>> updateItem(UpdateItemRequest request) {
        final String path = "/rest/v1/items";
        return webClient.post()
                .uri(path)
                .headers(headers -> {
                    headers.add(AUTHORIZATION, authHeaderToken);
                    headers.add(APIKEY, authHeaderToken);
                    headers.add(CONTENT_TYPE, APPLICATION_JSON);
                    headers.add(PREFER, RETURN_REPRESENTATION);
                    headers.add(PREFER, RESOLUTION_MERGE_DUPLICATES);
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
                .toEntity(String.class)
                .handle((responseEntity, sink) -> {
                    String responseBody = responseEntity.getBody();
                    String newBody = stringUtils.stripSurroundingBrackets(responseBody);
                    ResponseEntity<String> newResponse = new ResponseEntity<>(
                            newBody,
                            responseEntity.getHeaders(),
                            responseEntity.getStatusCode());
                    sink.next(newResponse);
                });
    }

    @Override
    public Mono<ResponseEntity<String>> patchItemById(String id, CreateItemRequest request) {
        final String path = "/rest/v1/items?_id=eq." + id;
        return webClient.patch()
                .uri(path)
                .headers(headers -> {
                    headers.add(AUTHORIZATION, authHeaderToken);
                    headers.add(APIKEY, authHeaderToken);
                    headers.add(CONTENT_TYPE, APPLICATION_JSON);
                    headers.add(PREFER, RETURN_REPRESENTATION);
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
                .toEntity(String.class)
                .handle((responseEntity, sink) -> {
                    if ("[]".equals(responseEntity.getBody())) {
                        sink.error(new EntityNotFoundException("Item"));
                        return;
                    }
                    String responseBody = responseEntity.getBody();
                    String newBody = stringUtils.stripSurroundingBrackets(responseBody);
                    ResponseEntity<String> newResponse = new ResponseEntity<>(
                            newBody,
                            responseEntity.getHeaders(),
                            responseEntity.getStatusCode());
                    sink.next(newResponse);
                });
    }

    @Override
    public Mono<ResponseEntity<String>> deleteItem(String id) {
        final String path = "/rest/v1/items?_id=eq." + id + "&select=*";
        return webClient.delete()
                .uri(path)
                .headers(headers -> {
                    headers.add(AUTHORIZATION, authHeaderToken);
                    headers.add(APIKEY, authHeaderToken);
                    headers.add(CONTENT_TYPE, APPLICATION_JSON);
                    headers.add(PREFER, RETURN_REPRESENTATION);
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
                .handle((responseEntity, sink) -> {
                    if ("[]".equals(responseEntity.getBody())) {
                        sink.error(new EntityNotFoundException("Item"));
                        return;
                    }
                    String responseBody = responseEntity.getBody();
                    String newBody = stringUtils.stripSurroundingBrackets(responseBody);
                    ResponseEntity<String> newResponse = new ResponseEntity<>(
                            newBody,
                            responseEntity.getHeaders(),
                            responseEntity.getStatusCode());
                    sink.next(newResponse);
                });
    }
}
