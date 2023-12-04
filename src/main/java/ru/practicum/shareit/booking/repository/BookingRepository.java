package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingProjection;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends PagingAndSortingRepository<Booking, Long> {

    List<Booking> findAllByBookerIdAndStatus(long userId, BookingStatus status, Sort sort);

    List<Booking> findAllByBookerId(long userId, Sort sort);

    List<Booking> findAllByBooker_IdAndEndBefore(long userId, LocalDateTime time, Sort sort);

    List<Booking> findAllByBooker_IdAndStartAfter(long userId, LocalDateTime time, Sort sort);

    List<Booking> findByItem_Owner_IdAndStatus(long userId, BookingStatus status, Sort sort);

    List<Booking> findAllByItem_Owner_Id(long userId, Sort sort);

    List<Booking> findAllByItem_Owner_IdAndEndBefore(long userId, LocalDateTime time, Sort sort);

    List<Booking> findAllByItem_Owner_IdAndStartAfter(long userId, LocalDateTime time, Sort sort);

    List<Booking> findAllByBooker_IdAndStartBeforeAndEndAfter(long userId, LocalDateTime before, LocalDateTime after);

    List<Booking> findAllByItem_Owner_IdAndStartBeforeAndEndAfter(long userId, LocalDateTime before, LocalDateTime after);

    BookingProjection findFirstByItem_Owner_IdAndStartBeforeAndStatus(long userId, LocalDateTime time, BookingStatus status, Sort sort);

    BookingProjection findFirstByItem_Owner_IdAndStartAfterAndStatus(long userId, LocalDateTime time, BookingStatus status, Sort sort);

    Boolean existsByItem_Id(long itemId);

    Boolean existsByBooker_IdAndItem_Id(long userId, long itemId);

    List<Booking> findByItem_IdAndBooker_IdAndStatus(long itemId, long userId, BookingStatus status);

    Boolean existsByBooker_IdAndItem_IdAndStartBefore(long userId, long itemId, LocalDateTime time);
}
