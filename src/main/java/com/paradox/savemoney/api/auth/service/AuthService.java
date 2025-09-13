package com.paradox.savemoney.api.auth.service;

import com.paradox.savemoney.api.auth.model.EmailAuthRequest;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface AuthService {
    Mono<ResponseEntity<String>> login(EmailAuthRequest request);
}
