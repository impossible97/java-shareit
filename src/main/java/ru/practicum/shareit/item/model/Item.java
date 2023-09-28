package ru.practicum.shareit.item.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Item {

    long id;
    @NotBlank
    @NotNull
    String title;
    @Size(max = 200, message = "Максимальная длинна описания 200 символов")
    @NotNull
    String description;
    @NotNull
    boolean available;
    @NotNull
    User owner;
}
