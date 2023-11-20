package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.ItemShort;
import ru.practicum.shareit.user.model.UserShort;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingDto {

    long id;
    @NotNull
    @FutureOrPresent
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime start;
    @NotNull
    @FutureOrPresent
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime end;

    BookingStatus status;
    @NotNull
    Long itemId;
    UserShort booker;
    ItemShort item;
}
