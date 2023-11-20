package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(long userId, BookingStatus status);

    List<Booking> findAllByBookerIdOrderByStartDesc(long userId);

    @Query("SELECT b FROM Booking b JOIN Item i ON b.item = i WHERE i.owner.id =:userId AND (:status IS NULL OR b.status =:status) ORDER BY b.start DESC")
    List<Booking> findAllByOwnerAndStatus(@Param("userId") long userId, @Param("status") BookingStatus status);

    @Query("SELECT b FROM Booking b JOIN Item i ON b.item = i WHERE i.owner.id =:userId ORDER BY b.start DESC")
    List<Booking> findAllByOwnerId(@Param("userId") long userId);
}
