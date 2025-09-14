package com.paradox.savemoney.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dev")
public class DevController {

    @GetMapping("/test")
    public String test() {
        return "Hello World";
    }

    @GetMapping("/env")
    public ResponseEntity<Map<String, String>> env() {
        Map<String, String> env = new HashMap<>();
        env.put("SUPABASE_DB_URL", System.getenv("SUPABASE_DB_URL"));
        env.put("SUPABASE_API_KEY", System.getenv("SUPABASE_API_KEY"));
        env.put("AUTH_URL", System.getenv("AUTH_URL"));
        env.put("AUTH_API_KEY", System.getenv("AUTH_API_KEY"));
        env.put("PORT", System.getenv("PORT"));
        return ResponseEntity.ok(env);
    }
}
