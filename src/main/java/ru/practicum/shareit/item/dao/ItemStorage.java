package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemStorage {

    ItemDto addItem(ItemDto itemDto, long userId);

    ItemDto updateItem(long userId, long itemId, ItemDto itemDto);

    ItemDto getItem(long itemId);

    List<ItemDto> getAllItems(long userId);

    List<ItemDto> searchItem(String text);
}
