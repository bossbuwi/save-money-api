package com.paradox.savemoney.controller;

import com.paradox.savemoney.api.supabase.model.CreateItemRequest;
import com.paradox.savemoney.api.supabase.model.UpdateItemRequest;
import com.paradox.savemoney.api.supabase.service.SupabaseApiService;
import com.paradox.savemoney.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/items")
public class ItemController {
    private final SupabaseApiService supabaseApiService;
    private final ItemService itemService;

    public ItemController(SupabaseApiService supabaseApiService, ItemService itemService) {
        this.supabaseApiService = supabaseApiService;
        this.itemService = itemService;
    }

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
    public Mono<ResponseEntity<String>> updateItem(@Valid @RequestBody UpdateItemRequest request) {
        return supabaseApiService.updateItem(request);
    }

    @PatchMapping(value = "/{id}")
    public Mono<ResponseEntity<String>> patchItem(@PathVariable String id, @Valid @RequestBody CreateItemRequest request) {
        return supabaseApiService.patchItemById(id, request);
    }

    @DeleteMapping(value = "/{id}")
    public Mono<ResponseEntity<String>> deleteItemById(@PathVariable String id) {
        return supabaseApiService.deleteItem(id);
    }
}
