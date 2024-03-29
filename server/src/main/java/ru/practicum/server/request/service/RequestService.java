package ru.practicum.server.request.service;

import ru.practicum.server.request.dto.RequestDto;

import java.util.List;

public interface RequestService {
    RequestDto addRequest(long userid, RequestDto requestDto);

    List<RequestDto> getRequests(long userId);

    RequestDto getRequestById(long requestId, long userId);

    List<RequestDto> getAllRequests(int from, int size, long userId);
}
