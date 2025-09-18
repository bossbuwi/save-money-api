package com.paradox.savemoney.api.supabase.service;

import com.paradox.savemoney.api.supabase.model.CreateItemRequest;
import com.paradox.savemoney.api.supabase.model.UpdateItemRequest;
import com.paradox.savemoney.web.WebClientHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class SupabaseApiServiceImplTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.RequestBodySpec requestBodySpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private WebClientHelper webClientHelper;

    @InjectMocks
    private SupabaseApiServiceImpl supabaseApiService;

    private final String authToken = "bearer-token";
    private final String itemId = "item-123";

    @BeforeEach
    void setUp() {
        // Mock WebClient method returns
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(webClient.patch()).thenReturn(requestBodyUriSpec);
        when(webClient.delete()).thenReturn(requestHeadersUriSpec);

        // Mock method chaining
        when(requestHeadersUriSpec.uri(any(String.class))).thenReturn(requestHeadersSpec);
        when(requestBodyUriSpec.uri(any(String.class))).thenReturn(requestBodySpec);
        doReturn(requestHeadersSpec).when(requestBodySpec).bodyValue(any());
        when(requestHeadersSpec.headers(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
    }

    @Test
    void getAllItems_Success() {
        // Arrange
        String responseBody = "[{\"id\": \"1\", \"name\": \"Item 1\"}, {\"id\": \"2\", \"name\": \"Item 2\"}]";
        ResponseEntity<String> responseEntity = ResponseEntity.ok(responseBody);

        when(responseSpec.toEntity(String.class)).thenReturn(Mono.just(responseEntity));
        when(webClientHelper.isError(any())).thenReturn(false);

        // Mock processResponse to pass through the response
        doAnswer(invocation -> {
            ResponseEntity<String> response = invocation.getArgument(0);
            reactor.core.publisher.SynchronousSink<ResponseEntity<String>> sink = invocation.getArgument(1);
            sink.next(response);
            return null;
        }).when(webClientHelper).processResponse(any(), any(), eq(false));

        // Act & Assert
        StepVerifier.create(supabaseApiService.getAllItems(authToken))
                .expectNext(responseEntity)
                .verifyComplete();
    }

    @Test
    void getItem_Success() {
        // Arrange
        String responseBody = "{\"id\": \"item-123\", \"name\": \"Test Item\"}";
        ResponseEntity<String> responseEntity = ResponseEntity.ok(responseBody);

        when(responseSpec.toEntity(String.class)).thenReturn(Mono.just(responseEntity));
        when(webClientHelper.isError(any())).thenReturn(false);

        // Mock processResponse to pass through the response
        doAnswer(invocation -> {
            ResponseEntity<String> response = invocation.getArgument(0);
            reactor.core.publisher.SynchronousSink<ResponseEntity<String>> sink = invocation.getArgument(1);
            sink.next(response);
            return null;
        }).when(webClientHelper).processResponse(any(), any(), eq(true));

        // Act & Assert
        StepVerifier.create(supabaseApiService.getItem(authToken, itemId))
                .expectNext(responseEntity)
                .verifyComplete();
    }

    @Test
    void addItem_Success() {
        // Arrange
        CreateItemRequest request = new CreateItemRequest();
        request.setAmount(1.0);
        String responseBody = "{\"id\": \"new-item-id\", \"name\": \"Test Item\"}";
        ResponseEntity<String> responseEntity = ResponseEntity.ok(responseBody);

        // Properly mock the WebClient chain for POST requests
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri("/rest/v1/items")).thenReturn(requestBodySpec);

        // Make sure headers() returns requestBodySpec to allow chaining
        when(requestBodySpec.headers(any())).thenReturn(requestBodySpec);

        // Make sure bodyValue() returns requestHeadersSpec
        doReturn(requestHeadersSpec).when(requestBodySpec).bodyValue(request);

        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.toEntity(String.class)).thenReturn(Mono.just(responseEntity));

        when(webClientHelper.isError(any())).thenReturn(false);

        // Mock processResponse to pass through the response
        doAnswer(invocation -> {
            ResponseEntity<String> response = invocation.getArgument(0);
            reactor.core.publisher.SynchronousSink<ResponseEntity<String>> sink = invocation.getArgument(1);
            sink.next(response);
            return null;
        }).when(webClientHelper).processResponse(any(), any(), eq(false));

        // Act & Assert
        StepVerifier.create(supabaseApiService.addItem(authToken, request))
                .expectNext(responseEntity)
                .verifyComplete();
    }

    @Test
    void patchItemById_Success() {
        // Arrange
        UpdateItemRequest request = new UpdateItemRequest();
        request.setAmount(1.0);
        String responseBody = "{\"id\": \"item-123\", \"name\": \"Updated Item\"}";
        ResponseEntity<String> responseEntity = ResponseEntity.ok(responseBody);

        // Properly mock the WebClient chain for PATCH requests
        when(webClient.patch()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri("/rest/v1/items?_id=eq.item-123")).thenReturn(requestBodySpec);

        // Make sure headers() returns requestBodySpec to allow chaining
        when(requestBodySpec.headers(any())).thenReturn(requestBodySpec);

        // Make sure bodyValue() returns requestHeadersSpec
        doReturn(requestHeadersSpec).when(requestBodySpec).bodyValue(request);

        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.toEntity(String.class)).thenReturn(Mono.just(responseEntity));

        when(webClientHelper.isError(any())).thenReturn(false);

        // Mock processResponse to pass through the response
        doAnswer(invocation -> {
            ResponseEntity<String> response = invocation.getArgument(0);
            reactor.core.publisher.SynchronousSink<ResponseEntity<String>> sink = invocation.getArgument(1);
            sink.next(response);
            return null;
        }).when(webClientHelper).processResponse(any(), any(), eq(true));

        // Act & Assert
        StepVerifier.create(supabaseApiService.patchItemById(authToken, itemId, request))
                .expectNext(responseEntity)
                .verifyComplete();
    }

    @Test
    void deleteItem_Success() {
        // Arrange
        String responseBody = "{\"id\": \"item-123\", \"name\": \"Deleted Item\"}";
        ResponseEntity<String> responseEntity = ResponseEntity.ok(responseBody);

        // Properly mock the WebClient chain for DELETE requests
        when(webClient.delete()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/rest/v1/items?_id=eq.item-123&select=*")).thenReturn(requestHeadersSpec);

        // Make sure headers() returns requestHeadersSpec to allow chaining
        when(requestHeadersSpec.headers(any())).thenReturn(requestHeadersSpec);

        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.toEntity(String.class)).thenReturn(Mono.just(responseEntity));

        when(webClientHelper.isError(any())).thenReturn(false);

        // Mock processResponse to pass through the response
        doAnswer(invocation -> {
            ResponseEntity<String> response = invocation.getArgument(0);
            reactor.core.publisher.SynchronousSink<ResponseEntity<String>> sink = invocation.getArgument(1);
            sink.next(response);
            return null;
        }).when(webClientHelper).processResponse(any(), any(), eq(true));

        // Act & Assert
        StepVerifier.create(supabaseApiService.deleteItem(authToken, itemId))
                .expectNext(responseEntity)
                .verifyComplete();
    }
}
