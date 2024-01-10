package ru.practicum.server.item.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.server.booking.model.BookingProjection;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {

    long id;
    String name;
    String description;
    Boolean available;
    BookingProjection lastBooking;
    BookingProjection nextBooking;
    List<CommentDto> comments;
    Long requestId;
}
