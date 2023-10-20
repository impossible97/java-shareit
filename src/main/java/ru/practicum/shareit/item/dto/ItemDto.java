package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {

    long id;
    @NotBlank
    String name;
    @Size(max = 200, message = "Максимальная длинна описания 200 символов")
    @NotBlank
    String description;
    @NotNull
    Boolean available;
}
