package ru.practicum.shareit.request.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.RequestService;

@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestController {

    private final RequestService requestService;
    @PostMapping
    ItemRequestDto addRequest(@RequestHeader("X-Sharer-User-Id") long userid,
                              @RequestBody @Validated ItemRequestDto requestDto) {
        return requestService.addRequest(userid, requestDto);
    }
}
