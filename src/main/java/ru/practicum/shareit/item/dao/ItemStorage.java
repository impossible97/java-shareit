package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Set;

public interface ItemStorage {

    Item addItem(Item item);

    Item updateItem(long itemId, Item item);

    Item getItem(long itemId);

    List<Item> getAllItems(long userId);

    List<Item> searchItem(String text);

    Set<Long> getItemsIds();
}
