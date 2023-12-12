package ru.practicum.shareit.request.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.RequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestController {

    private final RequestService requestService;

    @PostMapping
    ItemRequestDto addRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                              @RequestBody @Validated ItemRequestDto requestDto) {
        return requestService.addRequest(userId, requestDto);
    }

    @GetMapping
    List<ItemRequestDto> getRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        return requestService.getRequests(userId);
    }

    @GetMapping("{requestId}")
    ItemRequestDto getRequestById(@PathVariable long requestId) {
        return requestService.getRequestById(requestId);
    }

    @GetMapping("all")
    List<ItemRequestDto> getAllRequests(
            @RequestParam(name = "from") int from,
            @RequestParam(name = "size") int size) {
        return requestService.getAllRequests(from, size);
    }
}
