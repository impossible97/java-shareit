package ru.practicum.shareit.item.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @GetMapping("{itemId}")
    public ItemDto getItem(@PathVariable long itemId, @RequestHeader("X-Sharer-User-Id") long userId,
                           @RequestParam(name = "from", defaultValue = "0") int from,
                           @RequestParam(name = "size", defaultValue = "20") int size) {
        return itemService.getItem(itemId, userId, from, size);
    }

    @GetMapping
    public List<ItemDto> getAllItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                     @RequestParam(name = "from", defaultValue = "0") int from,
                                     @RequestParam(name = "size", defaultValue = "20") int size) {
        return itemService.getAll(userId, from, size);
    }

    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") long userId, @Validated @RequestBody ItemDto itemDto) {
        return itemService.addItem(itemDto, userId);
    }

    @PatchMapping("{itemId}")
    public ItemDto updateItem(
            @RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId, @RequestBody ItemDto itemDto) {
        return itemService.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("search")
    public List<ItemDto> searchItem(@RequestParam String text,
                                    @RequestParam(name = "from", defaultValue = "0") int from,
                                    @RequestParam(name = "size", defaultValue = "20") int size) {
        return itemService.searchItem(text, from, size);
    }

    @PostMapping("{itemId}/comment")
    public CommentDto addComment(
            @RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId, @RequestBody @Validated CommentDto commentDto) {
        return itemService.addComment(itemId, userId, commentDto);
    }
}
