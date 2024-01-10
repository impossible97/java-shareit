package ru.practicum.server.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.server.item.model.ItemRequestProjection;
import ru.practicum.server.request.dto.RequestDto;
import ru.practicum.server.request.model.ItemRequest;
import ru.practicum.server.user.model.User;

import java.util.List;

@Component
public class RequestMapper {

    public RequestDto toDto(ItemRequest itemRequest, List<ItemRequestProjection> items) {
        RequestDto itemRequestDto = new RequestDto();

        itemRequestDto.setId(itemRequest.getId());
        itemRequestDto.setDescription(itemRequest.getDescription());
        itemRequestDto.setCreated(itemRequest.getCreated());
        itemRequestDto.setItems(items);
        return itemRequestDto;
    }

    public ItemRequest toEntity(RequestDto itemRequestDto, User user) {
        ItemRequest itemRequest = new ItemRequest();

        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setUser(user);
        return itemRequest;
    }
}
