package com.paradox.savemoney.api.auth.service;

import com.paradox.savemoney.api.auth.model.EmailAuthRequest;
import com.paradox.savemoney.web.WebClientHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private WebClient webClient;
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
    private AuthServiceImpl authService;
    private EmailAuthRequest authRequest;

    @BeforeEach
    void setUp() {
        authRequest = new EmailAuthRequest();
        authRequest.setEmail("test@example.com");
        authRequest.setPassword("password123");

        // Mock the WebClient chain
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(any(String.class))).thenReturn(requestBodySpec);

        // Use doReturn to handle generic types properly
        doReturn(requestHeadersSpec).when(requestBodySpec).bodyValue(any(EmailAuthRequest.class));

        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
    }

    @Test
    void login_Successful() {
        // Arrange
        String responseBody = "{\"token\": \"jwt-token\"}";
        ResponseEntity<String> responseEntity = ResponseEntity.ok(responseBody);
        when(responseSpec.toEntity(String.class)).thenReturn(Mono.just(responseEntity));

        // Mock processResponse to just pass through the response
        doAnswer(invocation -> {
            ResponseEntity<String> response = invocation.getArgument(0);
            reactor.core.publisher.SynchronousSink<ResponseEntity<String>> sink = invocation.getArgument(1);
            sink.next(response);
            return null;
        }).when(webClientHelper).processResponse(any(), any(), eq(false));

        // Act & Assert
        StepVerifier.create(authService.login(authRequest))
                .expectNext(responseEntity)
                .verifyComplete();
    }
}
