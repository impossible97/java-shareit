package ru.practicum.server.booking.model;

import java.time.LocalDateTime;

public interface BookingProjection {

    Long getId();

    Long getBookerId();

    BookingStatus getStatus();

    LocalDateTime getStart();
}
