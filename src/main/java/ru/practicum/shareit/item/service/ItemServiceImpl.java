package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.repository.CommentRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.mapper.CommentMapper;
import ru.practicum.shareit.mapper.ItemMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemMapper itemMapper;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Override
    public ItemDto addItem(ItemDto itemDto, long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id= " + userId + " не найден"));

        return itemMapper.toDto(itemRepository.save(itemMapper.toEntity(itemDto, user)), null, null, new ArrayList<>());
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
        return itemMapper.toDto(itemRepository.save(item), null, null, new ArrayList<>());
    }

    @Override
    public ItemDto getItem(long itemId, long userId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new NotFoundException("Вещь с таким id = " + itemId + " не найдена"));
        List<CommentDto> commentsDto = commentRepository.findAllByAuthor_Id(userId).stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
        if (item.getOwner().getId() == userId) {
            return itemMapper.toDto(item,
                    bookingRepository.findFirstByItem_Owner_IdOrderByIdAsc(userId),
                    bookingRepository.findFirstByItem_Owner_IdOrderByIdDesc(userId),
                    commentsDto
                    );
        } else {
            return itemMapper.toDto(item, null, null, commentsDto);
        }
    }

    @Override
    public List<ItemDto> getAll(long userId) {
        List<CommentDto> commentsDto = commentRepository.findAllByAuthor_Id(userId).stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
        return itemRepository.findAll().stream()
                .filter(item -> item.getOwner().getId() == userId)
                .map(item -> {
                    if (bookingRepository.existsByItem_Id(item.getId())) {
                        return itemMapper.toDto(
                                item,
                                bookingRepository.findFirstByItem_Owner_IdOrderByIdAsc(userId),
                                bookingRepository.findFirstByItem_Owner_IdOrderByIdDesc(userId),
                                commentsDto);
                    } else {
                        return itemMapper.toDto(
                                item,
                                null,
                                null,
                                commentsDto
                        );
                    }
                })
                .sorted(Comparator.comparing(ItemDto::getId))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.findByText(text).stream()
                .filter(Item::getAvailable)
                .map(item -> itemMapper.toDto(
                                item,
                                null,
                               null,
                                new ArrayList<>()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto addComment(long itemId, long userId, CommentDto commentDto) {
        if (!bookingRepository.existsByItem_Owner_Id(userId)) {
            throw new NotFoundException("Бронирование пользователя с id = " + userId + " не найдено");
        }
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с таким id = " + userId + " не найден"));
        Comment comment = commentRepository.save(commentMapper.toEntity(commentDto, user));

        return commentMapper.toDto(comment);
    }
}