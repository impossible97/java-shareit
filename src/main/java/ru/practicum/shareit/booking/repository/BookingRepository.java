package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingProjection;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(long userId, BookingStatus status);

    List<Booking> findAllByBookerIdOrderByStartDesc(long userId);

    List<Booking> findAllByBooker_IdAndEndBeforeOrderByStartDesc(long userId, LocalDateTime time);

    List<Booking> findAllByBooker_IdAndStartAfterOrderByStartDesc(long userId, LocalDateTime time);

    List<Booking> findByItem_Owner_IdAndStatusOrderByStartDesc(long userId, BookingStatus status);

    List<Booking> findAllByItem_Owner_IdOrderByStartDesc(long userId);

    List<Booking> findAllByItem_Owner_IdAndEndBeforeOrderByStartDesc(long userId, LocalDateTime time);

    List<Booking> findAllByItem_Owner_IdAndStartAfterOrderByStartDesc(long userId, LocalDateTime time);

    List<Booking> findAllByBooker_IdAndStartBeforeAndEndAfter(long userId, LocalDateTime before, LocalDateTime after);

    List<Booking> findAllByItem_Owner_IdAndStartBeforeAndEndAfter(long userId, LocalDateTime before, LocalDateTime after);

    BookingProjection findFirstByItem_Owner_IdAndStartBeforeOrderByStartDesc(long userId, LocalDateTime time);

    BookingProjection findFirstByItem_Owner_IdAndStartAfterOrderByStartAsc(long userId, LocalDateTime time);

    Boolean existsByItem_Id(long itemId);

    Boolean existsByBooker_IdAndItem_Id(long userId, long itemId);

    List<Booking> findByItem_IdAndBooker_Id(long itemId, long userId);

    Boolean existsByBooker_IdAndItem_IdAndStartBefore(long userId, long itemId, LocalDateTime time);
}
