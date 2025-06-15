package com.paradox.savemoney.api.supabase.service;

import com.paradox.savemoney.api.supabase.model.CreateItemRequest;
import com.paradox.savemoney.api.supabase.model.ItemBase;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface SupabaseApiService {
    Mono<ResponseEntity<String>> getAllItems();
    Mono<ResponseEntity<String>> getItem(String id);
    Mono<ResponseEntity<String>> addItem(CreateItemRequest request);
    Mono<String> editItem(ItemBase itemBase);
    Mono<String> deleteItem(long id);
}
