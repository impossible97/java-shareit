package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemStorage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.mapper.ItemMapper;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation.ItemValidator;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final ItemMapper itemMapper;
    private final UserService userService;
    private final ItemValidator validator;

    @Override
    public ItemDto addItem(ItemDto itemDto, long userId) {
        return itemMapper.toDto(itemStorage.addItem(itemMapper.toEntity(itemDto, userService.getUser(userId))));
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        validator.validateId(itemId);
        validator.validateOwner(itemId, userId);
        return itemMapper.toDto(itemStorage.updateItem(itemId, itemMapper.toEntity(itemDto, userService.getUser(userId))));
    }

    @Override
    public ItemDto getItem(long itemId) {
        validator.validateId(itemId);
        return itemMapper.toDto(itemStorage.getItem(itemId));
    }

    @Override
    public List<ItemDto> getAll(long userId) {
        return itemStorage.getAllItems(userId).stream()
                .map(itemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        return itemStorage.searchItem(text).stream()
                .map(itemMapper::toDto)
                .collect(Collectors.toList());
    }
}
