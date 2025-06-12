package com.paradox.savemoney.api.supabase;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.paradox.savemoney.api.supabase.structure.SupabaseStructure.HeaderKey.APIKEY;
import static com.paradox.savemoney.config.structure.HttpStructure.HeaderKey.*;
import static com.paradox.savemoney.config.structure.HttpStructure.HeaderValue.*;

@Service
public class SupabaseApiService {
    private final WebClient webClient;
    @Value("${api.supabase.key}")
    private String authHeaderToken;

    public SupabaseApiService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<String> getAllItems() {
        final String selectAllItems = "/rest/v1/items?select=*";
        return webClient.get()
                .uri(selectAllItems)
                .header(AUTHORIZATION, AUTHORIZATION_BEARER + authHeaderToken)
                .header(APIKEY, authHeaderToken)
                .retrieve()
                .bodyToMono(String.class);
    }
}
