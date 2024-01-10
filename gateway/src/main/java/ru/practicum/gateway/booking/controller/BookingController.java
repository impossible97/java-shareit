package ru.practicum.gateway.booking.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.gateway.booking.client.BookingClient;
import ru.practicum.gateway.booking.dto.BookingDto;
import ru.practicum.gateway.booking.dto.BookingStatus;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;


@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
@Validated
@Slf4j
public class BookingController {

    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> addBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                     @Validated @RequestBody BookingDto bookingDto) {
        return bookingClient.addBooking(userId, bookingDto);
    }

    @PatchMapping("{bookingId}")
    public ResponseEntity<Object> changeBookingStatus(@RequestHeader("X-Sharer-User-Id") long userId,
                                          @RequestParam("approved") Boolean approved,
                                          @PathVariable("bookingId") Long bookingId) {
        return bookingClient.changeBookingStatus(userId, approved, bookingId);
    }

    @GetMapping("{bookingId}")
    public ResponseEntity<Object> getBooking(@PathVariable("bookingId") Long bookingId,
                                 @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingClient.getBooking(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllBookingsByBooker(@RequestHeader("X-Sharer-User-Id") long userid,
                                                   @RequestParam(value = "state", defaultValue = "ALL") final BookingStatus state,
                                                   @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                                   @Positive @RequestParam(name = "size", defaultValue = "20") int size) {
        return bookingClient.getAllBookingsByBooker(userid, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllBookingsByOwner(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                                  @RequestParam(value = "state", defaultValue = "ALL") final BookingStatus state,
                                                  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                                  @Positive @RequestParam(name = "size", defaultValue = "20") int size) {
        return bookingClient.getAllBookingsByOwner(ownerId, state, from, size);
    }
}