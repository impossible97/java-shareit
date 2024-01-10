package ru.practicum.gateway.user.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {

    long id;
    @NotBlank
    String name;
    @NotBlank(message = "Поле email не может быть пустым")
    @Email(message = "Неправильный адрес электронной почты")
    String email;
}
