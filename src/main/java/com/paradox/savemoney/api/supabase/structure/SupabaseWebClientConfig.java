package com.paradox.savemoney.api.supabase.structure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import static com.paradox.savemoney.api.supabase.structure.SupabaseStructure.HeaderKey.APIKEY;
import static com.paradox.savemoney.web.HttpStructure.HeaderKey.ACCEPT;
import static com.paradox.savemoney.web.HttpStructure.HeaderValue.APPLICATION_JSON;

@Configuration
public class SupabaseWebClientConfig {
    @Value("${api.supabase.url}")
    private String supabaseUri;
    @Value("${api.supabase.key}")
    private String supabaseApiKey;

    @Bean("supabaseWebClient")
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(supabaseUri)
                .defaultHeaders(headers -> {
                    headers.add(ACCEPT, APPLICATION_JSON);
                    headers.add(APIKEY, supabaseApiKey);
                })
                .build();
    }
}
