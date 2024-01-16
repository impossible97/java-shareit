package ru.practicum.server.booking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.server.booking.model.BookingStatus;
import ru.practicum.server.item.model.ItemProjection;
import ru.practicum.server.user.model.UserProjection;

import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingDto {

    long id;
    LocalDateTime start;
    LocalDateTime end;
    BookingStatus status;
    Long itemId;
    UserProjection booker;
    ItemProjection item;
}
