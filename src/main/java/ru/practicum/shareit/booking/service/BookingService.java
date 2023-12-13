package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.util.List;

public interface BookingService {

    BookingDto addBooking(long userId, BookingDto bookingDto);

    BookingDto changeBookingStatus(long userId, Boolean approved, Long bookingId);

    BookingDto getBooking(Long bookingId, long userId);

    List<BookingDto> getAllBookingsByBooker(long userid, BookingStatus state, int from, int size);

    List<BookingDto> getAllBookingsByOwner(long userid, BookingStatus state, int from, int size);
}
