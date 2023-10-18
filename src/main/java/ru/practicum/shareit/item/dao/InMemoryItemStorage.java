package ru.practicum.shareit.item.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.mapper.ItemMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InMemoryItemStorage implements ItemStorage {

    private final Map<Long, Item> items = new HashMap<>();
    private final ItemMapper itemMapper;
    private Long generatedId = 0L;

    @Autowired
    public InMemoryItemStorage(ItemMapper itemMapper) {
        this.itemMapper = itemMapper;
    }

    @Override
    public ItemDto addItem(ItemDto itemDto, long userId) {

        Item item = itemMapper.toEntity(itemDto, userId);
        itemDto.setId(++generatedId);
        item.setId(itemDto.getId());
        items.put(item.getId(), item);
        return itemDto;
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        if (!items.containsKey(itemId)) {
            throw new NotFoundException("Вещь с таким id = " + itemId + " не найдена!");
        }
        if (!(items.get(itemId).getOwner().getId() == userId)) {
            throw new NotFoundException("Вещь с таким id = " + itemId + " принадлежит другому пользователю!");
        }
        Item item = items.get(itemId);
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        items.put(item.getId(), item);

        return itemMapper.toDto(item);
    }

    @Override
    public ItemDto getItem(long itemId) {
        if (!items.containsKey(itemId)) {
            throw new NotFoundException("Вещь с таким id = " + itemId + " не найдена!");
        }
        return itemMapper.toDto(items.get(itemId));
    }

    @Override
    public List<ItemDto> getAllItems(long userId) {
        ArrayList<Item> itemsByUser = new ArrayList<>(items.values());

        return itemsByUser.stream()
                .filter(item -> item.getOwner().getId() == userId)
                .map(itemMapper::toDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        ArrayList<Item> itemsForSearch = new ArrayList<>(items.values());

        return itemsForSearch.stream()
                .filter(item -> (item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                        && item.getAvailable())
                .map(itemMapper::toDto)
                .collect(Collectors.toList());
    }
}