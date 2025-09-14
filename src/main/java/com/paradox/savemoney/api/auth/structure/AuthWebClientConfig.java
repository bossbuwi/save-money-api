package com.paradox.savemoney.api.auth.structure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import static com.paradox.savemoney.api.supabase.structure.SupabaseStructure.HeaderKey.APIKEY;
import static com.paradox.savemoney.web.HttpStructure.HeaderKey.ACCEPT;
import static com.paradox.savemoney.web.HttpStructure.HeaderValue.APPLICATION_JSON;

@Configuration
public class AuthWebClientConfig {
    @Value("${api.auth.url}")
    private String authUri;
    @Value("${api.auth.key}")
    private String authApiKey;

    @Bean("authWebClient")
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(authUri)
                .defaultHeaders(headers -> {
                    headers.add(ACCEPT, APPLICATION_JSON);
                    headers.add(APIKEY, authApiKey);
                })
                .build();
    }
}
