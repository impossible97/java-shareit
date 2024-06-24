package ru.practicum.server.booking.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.practicum.server.item.model.Item;
import ru.practicum.server.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "bookings", schema = "public")
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    Item item;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    User booker;
    @Column(name = "start_datatime")
    LocalDateTime start;
    @Column(name = "end_datatime")
    LocalDateTime end;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    BookingStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return id == booking.id && Objects.equals(item, booking.item) && Objects.equals(booker, booking.booker) && Objects.equals(start, booking.start) && Objects.equals(end, booking.end) && status == booking.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, item, booker, start, end, status);
    }
}