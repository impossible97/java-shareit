package ru.practicum.shareit.request.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@ToString
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @NotNull
    @Column(name = "description")
    String description;
    @Column(name = "created_time")
    LocalDateTime created = LocalDateTime.now();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    User user;
}