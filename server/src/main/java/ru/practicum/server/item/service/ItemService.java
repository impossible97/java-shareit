package ru.practicum.server.item.service;


import ru.practicum.server.item.dto.CommentDto;
import ru.practicum.server.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto addItem(ItemDto itemDto, long userid);

    ItemDto updateItem(long userId, long itemId, ItemDto itemDto);

    ItemDto getItem(long itemId, long userId);

    List<ItemDto> getAll(long userId, int from, int size);

    List<ItemDto> searchItem(String text, int from, int size);

    CommentDto addComment(long itemId, long userId, CommentDto commentDto);
}
