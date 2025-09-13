package com.paradox.savemoney.api.supabase.service;

import com.paradox.savemoney.api.supabase.model.CreateItemRequest;
import com.paradox.savemoney.api.supabase.model.UpdateItemRequest;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface SupabaseApiService {
    Mono<ResponseEntity<String>> getAllItems(String authToken);
    Mono<ResponseEntity<String>> getItem(String authToken, String id);
    Mono<ResponseEntity<String>> addItem(String authToken, CreateItemRequest request);
    Mono<ResponseEntity<String>> patchItemById(String authToken, String id, UpdateItemRequest request);
    Mono<ResponseEntity<String>> deleteItem(String authToken, String id);
}
