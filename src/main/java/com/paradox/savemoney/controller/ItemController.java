package com.paradox.savemoney.controller;

import com.paradox.savemoney.api.supabase.model.CreateItemRequest;
import com.paradox.savemoney.api.supabase.model.UpdateItemRequest;
import com.paradox.savemoney.api.supabase.service.SupabaseApiService;
import com.paradox.savemoney.util.HttpUtils;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/items")
public class ItemController {
    private final SupabaseApiService supabaseApiService;

    public ItemController(SupabaseApiService supabaseApiService) {
        this.supabaseApiService = supabaseApiService;
    }

    @GetMapping(value = "/index")
    public Mono<ResponseEntity<String>> getAllItems(@RequestHeader("Authorization") String authHeader) {
        String token = HttpUtils.extractBearerToken(authHeader);
        return supabaseApiService.getAllItems(token);
    }

    @GetMapping(value = "/{id}")
    public Mono<ResponseEntity<String>> getItemById(@RequestHeader("Authorization") String authHeader,
                                                    @PathVariable String id) {
        String token = HttpUtils.extractBearerToken(authHeader);
        return supabaseApiService.getItem(token,id);
    }

    @PostMapping(value = "/")
    public Mono<ResponseEntity<String>> addItem(@RequestHeader("Authorization") String authHeader,
                                                @Valid @RequestBody CreateItemRequest request) {
        String token = HttpUtils.extractBearerToken(authHeader);
        return supabaseApiService.addItem(token,request);
    }

    @PatchMapping(value = "/{id}")
    public Mono<ResponseEntity<String>> patchItem(@RequestHeader("Authorization") String authHeader,
                                                  @PathVariable String id,
                                                  @RequestBody UpdateItemRequest request) {
        String token = HttpUtils.extractBearerToken(authHeader);
        return supabaseApiService.patchItemById(token, id, request);
    }

    @DeleteMapping(value = "/{id}")
    public Mono<ResponseEntity<String>> deleteItemById(@RequestHeader("Authorization") String authHeader,
                                                       @PathVariable String id) {
        String token = HttpUtils.extractBearerToken(authHeader);
        return supabaseApiService.deleteItem(token, id);
    }
}
