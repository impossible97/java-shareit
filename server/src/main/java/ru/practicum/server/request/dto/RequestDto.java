package ru.practicum.server.request.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.server.item.model.ItemRequestProjection;

import java.time.LocalDateTime;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestDto {

    long id;
    String description;
    LocalDateTime created;
    List<ItemRequestProjection> items;
}
