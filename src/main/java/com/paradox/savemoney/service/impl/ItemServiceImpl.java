package com.paradox.savemoney.service.impl;

import com.paradox.savemoney.api.supabase.SupabaseApiService;
import com.paradox.savemoney.entity.Item;
import com.paradox.savemoney.exception.EntityNotFoundException;
import com.paradox.savemoney.repository.ItemRepository;
import com.paradox.savemoney.service.ItemService;
import jakarta.inject.Inject;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    @Inject
    private SupabaseApiService supabaseApiService;

    @Inject
    ItemRepository itemRepository;

    @Override
    public String test() {
        return "Hello World!";
    }

    @Override
    public String supaTest() {
        return supabaseApiService.getAllItems().block(Duration.ofSeconds(2));
    }

    @Override
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    @Override
    public Item getItemById(long id) {
        return itemRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Item"));
    }

    @Override
    public Item addItem(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public Item editItem(long id, Item newItem) {
        return itemRepository.findById(id).map(item -> {
            item.setAmount(newItem.getAmount());
            item.setDescription(newItem.getDescription());
            return itemRepository.save(item);
        }).orElseGet(() -> itemRepository.save(newItem));
    }

    @Override
    public void deleteItem(long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Item"));
        itemRepository.delete(item);
    }
}
