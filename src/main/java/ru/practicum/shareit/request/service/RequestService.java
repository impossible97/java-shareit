package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface RequestService {
    ItemRequestDto addRequest(long userid, ItemRequestDto requestDto);

    List<ItemRequestDto> getRequests(long userId);

    ItemRequestDto getRequestById(long requestId);

    List<ItemRequestDto> getAllRequests(int from, int size);
}
