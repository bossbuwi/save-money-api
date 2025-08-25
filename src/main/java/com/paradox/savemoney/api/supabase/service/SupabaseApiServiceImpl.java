package com.paradox.savemoney.api.supabase.service;

import com.paradox.savemoney.api.supabase.model.CreateItemRequest;
import com.paradox.savemoney.api.supabase.model.UpdateItemRequest;
import com.paradox.savemoney.exception.EntityNotFoundException;
import com.paradox.savemoney.exception.UpstreamApiException;
import com.paradox.savemoney.util.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;

import static com.paradox.savemoney.api.supabase.structure.SupabaseStructure.HeaderKey.APIKEY;
import static com.paradox.savemoney.api.supabase.structure.SupabaseStructure.HeaderKey.PREFER;
import static com.paradox.savemoney.api.supabase.structure.SupabaseStructure.HeaderValue.RETURN_REPRESENTATION;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
public class SupabaseApiServiceImpl implements SupabaseApiService {
    @Value("${api.supabase.key}")
    private String authHeaderToken;
    private final WebClient webClient;
    private final StringUtils stringUtils;

    public SupabaseApiServiceImpl(@Qualifier("supabaseWebClient") WebClient webClient,
                                  StringUtils stringUtils) {
        this.webClient = webClient;
        this.stringUtils = stringUtils;
    }

    @Override
    public Mono<ResponseEntity<String>> getAllItems() {
        final String path = "/rest/v1/items?select=*";
        return webClient.get()
                .uri(path)
                .headers(this::setCommonHeaders)
                .retrieve()
                .onStatus(this::isError, this::handleError)
                .toEntity(String.class);
    }

    @Override
    public Mono<ResponseEntity<String>> getItem(String id) {
        final String path = "/rest/v1/items?_id=eq." + id;
        return webClient.get()
                .uri(path)
                .headers(this::setCommonHeaders)
                .retrieve()
                .onStatus(this::isError, this::handleError)
                .toEntity(String.class)
                .handle((responseEntity, sink) ->
                        processResponse(responseEntity, sink, true));
    }

    @Override
    public Mono<ResponseEntity<String>> addItem(CreateItemRequest request) {
        final String path = "/rest/v1/items";
        return webClient.post()
                .uri(path)
                .headers(headers -> {
                    setCommonHeaders(headers);
                    headers.add(PREFER, RETURN_REPRESENTATION);
                })
                .bodyValue(request)
                .retrieve()
                .onStatus(this::isError, this::handleError)
                .toEntity(String.class)
                .handle((responseEntity, sink) ->
                        processResponse(responseEntity, sink, false));
    }

    @Override
    public Mono<ResponseEntity<String>> patchItemById(String id, UpdateItemRequest request) {
        final String path = "/rest/v1/items?_id=eq." + id;
        return webClient.patch()
                .uri(path)
                .headers(headers -> {
                    setCommonHeaders(headers);
                    headers.add(PREFER, RETURN_REPRESENTATION);
                })
                .bodyValue(request)
                .retrieve()
                .onStatus(this::isError, this::handleError)
                .toEntity(String.class)
                .handle((responseEntity, sink) ->
                        processResponse(responseEntity, sink, true));
    }

    @Override
    public Mono<ResponseEntity<String>> deleteItem(String id) {
        final String path = "/rest/v1/items?_id=eq." + id + "&select=*";
        return webClient.delete()
                .uri(path)
                .headers(headers -> {
                    setCommonHeaders(headers);
                    headers.add(PREFER, RETURN_REPRESENTATION);
                })
                .retrieve()
                .onStatus(this::isError, this::handleError)
                .toEntity(String.class)
                .handle((responseEntity, sink) ->
                        processResponse(responseEntity, sink, true));
    }

    private void setCommonHeaders(HttpHeaders headers) {
        headers.add(AUTHORIZATION, authHeaderToken);
        headers.add(APIKEY, authHeaderToken);
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

        String newBody = stringUtils.stripSurroundingBrackets(responseBody);

        ResponseEntity<String> newResponse = new ResponseEntity<>(
                newBody,
                responseEntity.getHeaders(),
                responseEntity.getStatusCode());
        sink.next(newResponse);
    }
}
