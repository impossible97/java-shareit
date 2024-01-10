package ru.practicum.gateway.request.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestDto {

    @NotBlank
    @Size(max = 200, message = "Максимальная длинна описания 200 символов")
    String description;
    @FutureOrPresent
    LocalDateTime created;
}
