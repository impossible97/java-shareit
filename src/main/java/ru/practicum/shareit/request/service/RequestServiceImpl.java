package ru.practicum.shareit.request.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.mapper.ItemMapper;
import ru.practicum.shareit.mapper.RequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestMapper requestMapper;
    private final UserRepository userRepository;
    private final ItemRequestRepository requestRepository;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Transactional
    @Override
    public ItemRequestDto addRequest(long userId, ItemRequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с таким id = " + userId + " не найден"));
        ItemRequest itemRequest = requestRepository.save(requestMapper.toEntity(requestDto, user));
        return requestMapper.toDto(itemRequest, new ArrayList<>());
    }

    @Override
    public List<ItemRequestDto> getRequests(long userId) {
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с таким id = " + userId + " не найден"));

        return requestRepository.findAllByUser_Id(userId, Sort.by("created").ascending())
                .stream()
                .map(itemRequest ->  requestMapper.toDto(itemRequest, itemRepository.findAllByRequest_Id(itemRequest.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto getRequestById(long requestId) {
        ItemRequest request = requestRepository.findById(requestId).orElseThrow(() ->
                new NotFoundException("Запроса с таким id = " + requestId + " не существует"));
        return requestMapper.toDto(request, itemRepository.findAllByRequest_Id(requestId));
    }

    @Override
    public List<ItemRequestDto> getAllRequests(int from, int size) {
        return requestRepository.findAll(PageRequest.of(from, size, Sort.by("created").ascending()))
                .stream()
                .map(itemRequest -> requestMapper.toDto(itemRequest, itemRepository.findAllByRequest_Id(itemRequest.getId())))
                .collect(Collectors.toList());
    }
}
