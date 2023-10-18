package ru.practicum.shareit.user.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    long id;
    @NotNull
    @NotBlank
    String name;
    @NotEmpty(message = "Поле email не может быть пустым")
    @Email(message = "Неправильный адрес электронной почты")
    private String email;
}
