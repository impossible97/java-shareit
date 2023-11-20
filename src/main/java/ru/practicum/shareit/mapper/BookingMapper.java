package ru.practicum.shareit.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemShort;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserShort;

@Component
@AllArgsConstructor
public class BookingMapper {

    public BookingDto toDto(Booking booking, UserShort userShort, ItemShort itemShort) {
        BookingDto bookingDto = new BookingDto();

        bookingDto.setId(booking.getId());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setBooker(userShort);
        bookingDto.setItem(itemShort);
        bookingDto.setStatus(booking.getStatus());

        return bookingDto;
    }

    public Booking toEntity(BookingDto bookingDto, User user, Item item) {
        Booking booking = new Booking();

        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(bookingDto.getStatus());

        return booking;
    }
}
