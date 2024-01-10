package ru.practicum.server.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.server.booking.dto.BookingDto;
import ru.practicum.server.booking.model.Booking;
import ru.practicum.server.item.model.Item;
import ru.practicum.server.item.model.ItemProjection;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.model.UserProjection;

@Component
@AllArgsConstructor
public class BookingMapper {

    public BookingDto toDto(Booking booking, UserProjection userShort, ItemProjection itemShort) {
        BookingDto bookingDto = new BookingDto();

        bookingDto.setId(booking.getId());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setBooker(userShort);
        bookingDto.setItem(itemShort);
        bookingDto.setStatus(booking.getStatus());
        bookingDto.setItemId(booking.getItem().getId());

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
