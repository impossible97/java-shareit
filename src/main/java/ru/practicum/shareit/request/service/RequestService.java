package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

public interface RequestService {
    ItemRequestDto addRequest(long userid, ItemRequestDto requestDto);
}
