package com.paradox.savemoney.api.supabase.service;

import com.paradox.savemoney.api.supabase.model.CreateItemRequest;
import com.paradox.savemoney.util.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SupabaseApiServiceImplTest {

    @Mock
    private WebClient webClient;
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;
    @Mock
    private WebClient.RequestBodySpec requestBodySpec;
    @Mock
    private WebClient.ResponseSpec responseSpec;
    @Mock
    private StringUtils stringUtils;
    @InjectMocks
    private SupabaseApiServiceImpl supabaseApiService;

    @BeforeEach
    void setUp() {
        // Set the authHeaderToken value using ReflectionTestUtils
        ReflectionTestUtils.setField(supabaseApiService, "authHeaderToken", "Bearer test-token");
    }

    @Test
    void getAllItems_Success() {
        // Mock WebClient chain
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/rest/v1/items?select=*")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.headers(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.toEntity(String.class)).thenReturn(Mono.just(ResponseEntity.ok("[]")));

        // Execute and verify
        StepVerifier.create(supabaseApiService.getAllItems())
                .expectNextMatches(response -> response.getStatusCode().is2xxSuccessful())
                .verifyComplete();
    }

    @Test
    void getItem_Success() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/rest/v1/items?_id=eq.test-id")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.headers(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.toEntity(String.class)).thenReturn(Mono.just(ResponseEntity.ok("[{\"id\":\"test-id\"}]")));
        when(stringUtils.stripSurroundingBrackets("[{\"id\":\"test-id\"}]")).thenReturn("{\"id\":\"test-id\"}");

        StepVerifier.create(supabaseApiService.getItem("test-id"))
                .expectNextMatches(response -> response.getBody().equals("{\"id\":\"test-id\"}"))
                .verifyComplete();
    }

    @Test
    void addItem_Success() {
        CreateItemRequest request = new CreateItemRequest();
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri("/rest/v1/items")).thenReturn(requestBodySpec);
        when(requestBodySpec.headers(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(request)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.toEntity(String.class)).thenReturn(Mono.just(ResponseEntity.ok("[{\"id\":\"new-id\"}]")));
        when(stringUtils.stripSurroundingBrackets("[{\"id\":\"new-id\"}]")).thenReturn("{\"id\":\"new-id\"}");

        StepVerifier.create(supabaseApiService.addItem(request))
                .expectNextMatches(response -> response.getBody().equals("{\"id\":\"new-id\"}"))
                .verifyComplete();
    }
}
