package ru.practicum.shareit.request.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@Service
@AllArgsConstructor
public class RequestServiceImpl implements RequestService {

    @Override
    public ItemRequestDto addRequest(long userid, ItemRequestDto requestDto) {
        return null;
    }
}
