package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto addItem(ItemDto itemDto, long userid);

    ItemDto updateItem(long userId, long itemId, ItemDto itemDto);

    ItemDto getItem(long itemId, long userId);

    List<ItemDto> getAll(long userId);

    List<ItemDto> searchItem(String text);

    CommentDto addComment(long itemId, long userId, CommentDto commentDto);
}
