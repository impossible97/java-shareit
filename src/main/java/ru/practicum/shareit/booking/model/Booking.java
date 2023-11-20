package ru.practicum.shareit.booking.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings", schema = "public")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @OneToOne
    @JoinColumn(name = "item_id")
    Item item;
    @OneToOne
    @JoinColumn(name = "user_id")
    User booker;
    @Column(name = "start_datatime")
    LocalDateTime start;
    @Column(name = "end_datatime")
    LocalDateTime end;
    @Column(name = "status")
    BookingStatus status;
}
