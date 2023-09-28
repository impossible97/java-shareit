package ru.practicum.shareit.user;

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
    @Email(message = "Неправильный адрес электронной почты")
    @NotEmpty(message = "Поле email не может быть пустым")
    @NotNull
    private String email;
}
