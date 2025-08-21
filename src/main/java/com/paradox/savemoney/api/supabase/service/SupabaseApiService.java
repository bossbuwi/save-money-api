package com.paradox.savemoney.api.supabase.service;

import com.paradox.savemoney.api.supabase.model.CreateItemRequest;
import com.paradox.savemoney.api.supabase.model.UpdateItemRequest;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface SupabaseApiService {
    Mono<ResponseEntity<String>> getAllItems();
    Mono<ResponseEntity<String>> getItem(String id);
    Mono<ResponseEntity<String>> addItem(CreateItemRequest request);
    Mono<ResponseEntity<String>> updateItem(UpdateItemRequest request);
    Mono<ResponseEntity<String>> patchItemById(String id, CreateItemRequest request);
    Mono<ResponseEntity<String>> deleteItem(String id);
}
