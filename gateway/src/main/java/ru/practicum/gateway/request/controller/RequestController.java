package ru.practicum.gateway.request.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.gateway.request.client.RequestClient;
import ru.practicum.gateway.request.dto.RequestDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
@Validated
public class RequestController {

    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> addRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                     @RequestBody @Validated RequestDto requestDto) {
        return requestClient.addRequest(userId, requestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        return requestClient.getRequests(userId);
    }

    @GetMapping("{requestId}")
    public ResponseEntity<Object> getRequestById(@PathVariable long requestId,
                              @RequestHeader("X-Sharer-User-Id") long userId) {
        return requestClient.getRequestById(requestId, userId);
    }

    @GetMapping("all")
    public ResponseEntity<Object> getAllRequests(
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
            @Positive @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestHeader("X-Sharer-User-Id") long userId) {
        return requestClient.getAllRequests(from, size, userId);
    }
}
