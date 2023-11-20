package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.mapper.ItemMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemMapper itemMapper;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto addItem(ItemDto itemDto, long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id= " + userId + " не найден"));

        return itemMapper.toDto(itemRepository.save(itemMapper.toEntity(itemDto, user)));
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.orElseThrow(() -> new NotFoundException("Пользователь с id= " + userId + " не найден"));
        user.setId(userId);
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new NotFoundException("Вещь с таким id = " + itemId + " не найдена"));
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        return itemMapper.toDto(itemRepository.save(item));
    }

    @Override
    public ItemDto getItem(long itemId, long userId) {
        return itemMapper.toDto(itemRepository.findById(itemId).orElseThrow(() ->
                new NotFoundException("Вещь с таким id = " + itemId + " не найдена")));
    }

    @Override
    public List<ItemDto> getAll(long userId) {
        return itemRepository.findAll().stream()
                .filter(item -> item.getOwner().getId() == userId)
                .map(itemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.findByText(text).stream()
                .filter(Item::getAvailable)
                .map(itemMapper::toDto)
                .collect(Collectors.toList());
    }
}