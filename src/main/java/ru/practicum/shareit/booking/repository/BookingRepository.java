package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingProjection;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(long userId, BookingStatus status);

    List<Booking> findAllByBookerIdOrderByStartDesc(long userId);

    List<Booking> findByItem_Owner_IdAndStatusOrderByStartDesc(long userId, BookingStatus status);

    List<Booking> findAllByItem_Owner_IdOrderByStartDesc(long userId);

    BookingProjection findFirstByItem_Owner_IdOrderByIdDesc(long userId);

    BookingProjection findFirstByItem_Owner_IdOrderByIdAsc(long userId);

    Boolean existsByItem_Id(long itemId);

    Boolean existsByItem_Owner_Id(long userId);
}
