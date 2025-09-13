package com.paradox.savemoney.api.supabase.service;

import com.paradox.savemoney.api.supabase.model.CreateItemRequest;
import com.paradox.savemoney.api.supabase.model.UpdateItemRequest;
import com.paradox.savemoney.web.WebClientHelper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.paradox.savemoney.api.supabase.structure.SupabaseStructure.HeaderKey.PREFER;
import static com.paradox.savemoney.api.supabase.structure.SupabaseStructure.HeaderValue.RETURN_REPRESENTATION;

@Service
public class SupabaseApiServiceImpl implements SupabaseApiService {
    private final WebClient webClient;
    private final WebClientHelper webClientHelper;

    public SupabaseApiServiceImpl(@Qualifier("supabaseWebClient") WebClient webClient,
                                  WebClientHelper webClientHelper) {
        this.webClient = webClient;
        this.webClientHelper = webClientHelper;
    }

    @Override
    public Mono<ResponseEntity<String>> getAllItems(String authToken) {
        final String path = "/rest/v1/items?select=*";
        return webClient.get()
                .uri(path)
                .headers(headers -> headers.setBearerAuth(authToken))
                .retrieve()
                .onStatus(webClientHelper::isError, webClientHelper::handleError)
                .toEntity(String.class);
    }

    @Override
    public Mono<ResponseEntity<String>> getItem(String authToken, String id) {
        final String path = "/rest/v1/items?_id=eq." + id;
        return webClient.get()
                .uri(path)
                .headers(headers -> headers.setBearerAuth(authToken))
                .retrieve()
                .onStatus(webClientHelper::isError, webClientHelper::handleError)
                .toEntity(String.class)
                .handle((responseEntity, sink) ->
                        webClientHelper.processResponse(responseEntity, sink, true));
    }

    @Override
    public Mono<ResponseEntity<String>> addItem(String authToken, CreateItemRequest request) {
        final String path = "/rest/v1/items";
        return webClient.post()
                .uri(path)
                .headers(headers -> {
                    headers.setBearerAuth(authToken);
                    headers.add(PREFER, RETURN_REPRESENTATION);
                })
                .bodyValue(request)
                .retrieve()
                .onStatus(webClientHelper::isError, webClientHelper::handleError)
                .toEntity(String.class)
                .handle((responseEntity, sink) ->
                        webClientHelper.processResponse(responseEntity, sink, false));
    }

    @Override
    public Mono<ResponseEntity<String>> patchItemById(String authToken, String id, UpdateItemRequest request) {
        final String path = "/rest/v1/items?_id=eq." + id;
        return webClient.patch()
                .uri(path)
                .headers(headers -> {
                    headers.setBearerAuth(authToken);
                    headers.add(PREFER, RETURN_REPRESENTATION);
                })
                .bodyValue(request)
                .retrieve()
                .onStatus(webClientHelper::isError, webClientHelper::handleError)
                .toEntity(String.class)
                .handle((responseEntity, sink) ->
                        webClientHelper.processResponse(responseEntity, sink, true));
    }

    @Override
    public Mono<ResponseEntity<String>> deleteItem(String authToken, String id) {
        final String path = "/rest/v1/items?_id=eq." + id + "&select=*";
        return webClient.delete()
                .uri(path)
                .headers(headers -> {
                    headers.setBearerAuth(authToken);
                    headers.add(PREFER, RETURN_REPRESENTATION);
                })
                .retrieve()
                .onStatus(webClientHelper::isError, webClientHelper::handleError)
                .toEntity(String.class)
                .handle((responseEntity, sink) ->
                        webClientHelper.processResponse(responseEntity, sink, true));
    }
}
