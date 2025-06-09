package com.paradox.savemoney.controller;

import com.paradox.savemoney.entity.Item;
import com.paradox.savemoney.service.ItemService;
import jakarta.inject.Inject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    @Inject
    ItemService itemService;

    @GetMapping("/test")
    public String test() {
        return itemService.test();
    }

    @GetMapping
    public List<Item> getAllItems() {
        return itemService.getAllItems();
    }

    @GetMapping("/{id}")
    public Item getItemById(@PathVariable Long id) {
        return itemService.getItemById(id);
    }

    @PostMapping
    public Item addItem(@RequestBody Item newItem) {
        return itemService.addItem(newItem);
    }

    @PutMapping("/{id}")
    public Item editItem(@PathVariable Long id, @RequestBody Item newItem) {
        return itemService.editItem(id, newItem);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
    }
}
