package com.paradox.savemoney.service;

import com.paradox.savemoney.entity.Item;

import java.util.List;

public interface ItemService {
    String test();
    List<Item> getAllItems();
    Item getItemById(long id);
    Item addItem(Item item);
    Item editItem(long id, Item item);
    void deleteItem(long item);
}
