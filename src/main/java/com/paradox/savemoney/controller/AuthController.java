package com.paradox.savemoney.controller;

import com.paradox.savemoney.api.auth.model.EmailAuthRequest;
import com.paradox.savemoney.api.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<String>> login(@Valid @RequestBody EmailAuthRequest emailAuthRequest) {
        return authService.login(emailAuthRequest);
    }
}
