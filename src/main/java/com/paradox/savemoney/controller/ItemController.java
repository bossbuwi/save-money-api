package com.paradox.savemoney.controller;

import com.paradox.savemoney.api.supabase.model.CreateItemRequest;
import com.paradox.savemoney.api.supabase.model.UpdateItemRequest;
import com.paradox.savemoney.api.supabase.service.SupabaseApiService;
import com.paradox.savemoney.service.ItemService;
import jakarta.inject.Inject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/items")
public class ItemController {
    @Inject
    SupabaseApiService supabaseApiService;

    @Inject
    ItemService itemService;

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return new ResponseEntity<>(itemService.test(), HttpStatus.OK);
    }

    @GetMapping(value = "/index")
    public Mono<ResponseEntity<String>> getAllItems() {
        return supabaseApiService.getAllItems();
    }

    @GetMapping(value = "/{id}")
    public Mono<ResponseEntity<String>> getItemById(@PathVariable String id) {
        return supabaseApiService.getItem(id);
    }

    @PostMapping(value = "/")
    public Mono<ResponseEntity<String>> addItem(@RequestBody CreateItemRequest request) {
        return supabaseApiService.addItem(request);
    }

    @PutMapping(value = "/")
    public Mono<ResponseEntity<String>> updateItem(@RequestBody UpdateItemRequest request) {
        return supabaseApiService.editItem(request);
    }

    @PatchMapping(value = "/{id}")
    public Mono<ResponseEntity<String>> editItem(@PathVariable String id, @RequestBody CreateItemRequest request) {
        return supabaseApiService.editItemById(id, request);
    }
}
