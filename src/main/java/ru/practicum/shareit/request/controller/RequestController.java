package ru.practicum.shareit.request.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.service.RequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @PostMapping
    RequestDto addRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                              @RequestBody @Validated RequestDto requestDto) {
        return requestService.addRequest(userId, requestDto);
    }

    @GetMapping
    List<RequestDto> getRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        return requestService.getRequests(userId);
    }

    @GetMapping("{requestId}")
    RequestDto getRequestById(@PathVariable long requestId,
                              @RequestHeader("X-Sharer-User-Id") long userId) {
        return requestService.getRequestById(requestId, userId);
    }

    @GetMapping("all")
    List<RequestDto> getAllRequests(
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestHeader("X-Sharer-User-Id") long userId) {
        return requestService.getAllRequests(from, size, userId);
    }
}
