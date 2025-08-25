package com.paradox.savemoney.api.supabase.structure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import static com.paradox.savemoney.config.structure.HttpStructure.HeaderKey.*;
import static com.paradox.savemoney.config.structure.HttpStructure.HeaderValue.*;

@Configuration
public class SupabaseWebClientConfig {
    @Value("${api.supabase.url}")
    private String supabaseUri;

    @Bean("supabaseWebClient")
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(supabaseUri)
                .defaultHeader(ACCEPT, APPLICATION_JSON)
                .build();
    }
}
