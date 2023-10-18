package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemStorage;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;

    @Override
    public ItemDto addItem(ItemDto itemDto, long userId) {
        return itemStorage.addItem(itemDto, userId);
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        return itemStorage.updateItem(userId, itemId, itemDto);
    }

    @Override
    public ItemDto getItem(long itemId) {
        return itemStorage.getItem(itemId);
    }

    @Override
    public List<ItemDto> getAll(long userId) {
        return itemStorage.getAllItems(userId);
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        return itemStorage.searchItem(text);
    }
}
