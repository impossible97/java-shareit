package ru.practicum.server.request.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.server.exception.NotFoundException;
import ru.practicum.server.item.repository.ItemRepository;
import ru.practicum.server.mapper.RequestMapper;
import ru.practicum.server.request.dto.RequestDto;
import ru.practicum.server.request.model.ItemRequest;
import ru.practicum.server.request.repository.ItemRequestRepository;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.repository.UserRepository;

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

    @Transactional
    @Override
    public RequestDto addRequest(long userId, RequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с таким id = " + userId + " не найден"));
        ItemRequest itemRequest = requestRepository.save(requestMapper.toEntity(requestDto, user));
        return requestMapper.toDto(itemRequest, new ArrayList<>());
    }

    @Transactional(readOnly = true)
    @Override
    public List<RequestDto> getRequests(long userId) {
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с таким id = " + userId + " не найден"));

        return requestRepository.findAllByUser_Id(userId, Sort.by("created").ascending())
                .stream()
                .map(itemRequest ->  requestMapper.toDto(itemRequest, itemRepository.findAllByRequest_Id(itemRequest.getId())))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public RequestDto getRequestById(long requestId, long userId) {
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с таким id = " + userId + " не найден"));
        ItemRequest request = requestRepository.findById(requestId).orElseThrow(() ->
                new NotFoundException("Запроса с таким id = " + requestId + " не существует"));
        return requestMapper.toDto(request, itemRepository.findAllByRequest_Id(requestId));
    }

    @Transactional(readOnly = true)
    @Override
    public List<RequestDto> getAllRequests(int from, int size, long userId) {
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с таким id = " + userId + " не найден"));
        return requestRepository.findAll(PageRequest.of(from / size, size, Sort.by("created").ascending()))
                .stream()
                .filter(itemRequest -> userId != itemRequest.getUser().getId())
                .map(itemRequest -> requestMapper.toDto(itemRequest, itemRepository.findAllByRequest_Id(itemRequest.getId())))
                .collect(Collectors.toList());
    }
}
