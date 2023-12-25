package ru.practicum.shareit.booking.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;


@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
@Validated
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDto addBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                 @Validated @RequestBody BookingDto bookingDto) {

        return bookingService.addBooking(userId, bookingDto);
    }

    @PatchMapping("{bookingId}")
    public BookingDto changeBookingStatus(@RequestHeader("X-Sharer-User-Id") long userId,
                                          @RequestParam("approved") Boolean approved,
                                          @PathVariable("bookingId") Long bookingId) {
        return bookingService.changeBookingStatus(userId, approved, bookingId);
    }

    @GetMapping("{bookingId}")
    public BookingDto getBooking(@PathVariable("bookingId") Long bookingId,
                                 @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.getBooking(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getAllBookingsByBooker(@RequestHeader("X-Sharer-User-Id") long userid,
                                                   @RequestParam(value = "state", defaultValue = "ALL") final BookingStatus state,
                                                   @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                                   @Positive @RequestParam(name = "size", defaultValue = "20") int size) {
        return bookingService.getAllBookingsByBooker(userid, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllBookingsByOwner(@RequestHeader("X-Sharer-User-Id") long userid,
                                                  @RequestParam(value = "state", defaultValue = "ALL") final BookingStatus state,
                                                  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                                  @Positive @RequestParam(name = "size", defaultValue = "20") int size) {
        return bookingService.getAllBookingsByOwner(userid, state, from, size);
    }
}