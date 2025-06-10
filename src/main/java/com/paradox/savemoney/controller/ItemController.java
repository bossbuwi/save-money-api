package com.paradox.savemoney.controller;

import com.paradox.savemoney.entity.Item;
import com.paradox.savemoney.service.ItemService;
import jakarta.inject.Inject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    @Inject
    ItemService itemService;

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return new ResponseEntity<>(itemService.test(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Item>> getAllItems() {
        return new ResponseEntity<>(itemService.getAllItems(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        return new ResponseEntity<>(itemService.getItemById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Item> addItem(@RequestBody Item newItem) {
        return new ResponseEntity<>(itemService.addItem(newItem), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> editItem(@PathVariable Long id, @RequestBody Item newItem) {
        return new ResponseEntity<>(itemService.editItem(id, newItem), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}
