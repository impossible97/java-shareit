package ru.practicum.shareit.booking.service;


import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.mapper.BookingMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Transactional
    @Override
    public BookingDto addBooking(long userId, BookingDto bookingDto) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с таким id = " + userId + " не найден"));
        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(() ->
                new NotFoundException("Вещь с таким id = " + bookingDto.getItemId() + " не найдена"));
        if (userId == item.getOwner().getId()) {
            throw new NotFoundException("Пользователь с id = " + userId + " уже является хозяином вещи c id = " + item.getId());
        }
        if (item.getAvailable()) {
            bookingDto.setStatus(BookingStatus.WAITING);
        } else {
            throw new ValidationException("Вещь недоступна к бронированию");
        }
        if (bookingDto.getStart().isEqual(bookingDto.getEnd()) || bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            throw new ValidationException("Время конца бронирования не может быть меньше или равно времени старта бронирования");
        }
        Booking booking = bookingMapper.toEntity(bookingDto, user, item);
        bookingRepository.save(booking);

        return bookingMapper.toDto(booking,
                userRepository.findUserById(userId),
                itemRepository.findItemById(bookingDto.getItemId()));
    }

    @Transactional
    @Override
    public BookingDto changeBookingStatus(long userId, Boolean approved, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new NotFoundException("Бронирования с таким id = " + bookingId + " не существует"));
        if (!(booking.getItem().getOwner().getId() == userId)) {
            throw new NotFoundException("Пользователь с id = " + userId + " не является владельцем вещи");
        }
        if (booking.getStatus().equals(BookingStatus.APPROVED)) {
            throw new ValidationException("Бронирование уже подтверждено");
        }
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
            bookingRepository.save(booking);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
            bookingRepository.save(booking);
        }
        return bookingMapper.toDto(
                booking, userRepository.findUserById(booking.getBooker().getId()), itemRepository.findItemById(booking.getItem().getId()));
    }

    @Transactional(readOnly = true)
    @Override
    public BookingDto getBooking(Long bookingId, long userId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new NotFoundException("Бронирования с таким id = " + bookingId + " не существует"));
        if (!(booking.getBooker().getId() == userId || booking.getItem().getOwner().getId() == userId)) {
            throw new NotFoundException(
                    "Пользователь с id = " + userId + " не является владельцем вещи или автором бронирования");
        }
        return bookingMapper.toDto(
                booking, userRepository.findUserById(booking.getBooker().getId()),
                itemRepository.findItemById(booking.getItem().getId()));
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookingDto> getAllBookingsByBooker(long userId, BookingStatus state) {
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с таким id = " + userId + " не найден"));

        switch (state) {
            case ALL:
                return bookingRepository.findAllByBookerId(userId, Sort.by(Sort.Direction.DESC, "start"))
                        .stream()
                        .map(booking -> bookingMapper.toDto(
                                booking,
                                userRepository.findUserById(userId),
                                itemRepository.findItemById(booking.getItem().getId())))
                        .collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.findAllByBooker_IdAndStartAfter(
                        userId, LocalDateTime.now(), Sort.by(Sort.Direction.DESC, "start"))
                        .stream()
                        .map(booking -> bookingMapper.toDto(
                                booking,
                                userRepository.findUserById(userId),
                                itemRepository.findItemById(booking.getItem().getId())))
                        .collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.findAllByBooker_IdAndStartBeforeAndEndAfter(userId, LocalDateTime.now(), LocalDateTime.now())
                        .stream()
                        .map(booking -> bookingMapper.toDto(
                                booking,
                                userRepository.findUserById(userId),
                                itemRepository.findItemById(booking.getItem().getId())))
                        .collect(Collectors.toList());
            case PAST:
                return bookingRepository.findAllByBooker_IdAndEndBefore(
                        userId, LocalDateTime.now(), Sort.by(Sort.Direction.DESC, "start"))
                        .stream()
                        .map(booking -> bookingMapper.toDto(
                                booking,
                                userRepository.findUserById(userId),
                                itemRepository.findItemById(booking.getItem().getId())
                        ))
                        .collect(Collectors.toList());
            default:
                return bookingRepository.findAllByBookerIdAndStatus(
                        userId, state, Sort.by(Sort.Direction.DESC, "start"))
                        .stream()
                        .map(booking -> bookingMapper.toDto(
                                booking,
                                userRepository.findUserById(userId),
                                itemRepository.findItemById(booking.getItem().getId())))
                        .collect(Collectors.toList());
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookingDto> getAllBookingsByOwner(long ownerId, BookingStatus state) {

        userRepository.findById(ownerId).orElseThrow(() ->
                new NotFoundException("Пользователь с таким id = " + ownerId + " не найден"));

        if (state.equals(BookingStatus.ALL)) {
            return bookingRepository.findAllByItem_Owner_Id(ownerId, Sort.by(Sort.Direction.DESC, "start"))
                    .stream()
                    .map(booking -> bookingMapper.toDto(
                            booking,
                            userRepository.findUserById(booking.getBooker().getId()),
                            itemRepository.findItemById(booking.getItem().getId())
                    ))
                    .collect(Collectors.toList());
        } else if (state.equals(BookingStatus.FUTURE)) {
            return bookingRepository.findAllByItem_Owner_IdAndStartAfter(
                    ownerId, LocalDateTime.now(), Sort.by(Sort.Direction.DESC, "start"))
                    .stream()
                    .map(booking -> bookingMapper.toDto(
                            booking,
                            userRepository.findUserById(booking.getBooker().getId()),
                            itemRepository.findItemById(booking.getItem().getId())))
                    .collect(Collectors.toList());
        } else if (state.equals(BookingStatus.CURRENT)) {
            return bookingRepository.findAllByItem_Owner_IdAndStartBeforeAndEndAfter(ownerId, LocalDateTime.now(), LocalDateTime.now())
                    .stream()
                    .map(booking -> bookingMapper.toDto(
                            booking,
                            userRepository.findUserById(booking.getBooker().getId()),
                            itemRepository.findItemById(booking.getItem().getId())))
                    .collect(Collectors.toList());
        } else if (state.equals(BookingStatus.PAST)) {
            return bookingRepository.findAllByItem_Owner_IdAndEndBefore(
                    ownerId, LocalDateTime.now(), Sort.by(Sort.Direction.DESC, "start"))
                    .stream()
                    .map(booking -> bookingMapper.toDto(
                            booking,
                            userRepository.findUserById(booking.getBooker().getId()),
                            itemRepository.findItemById(booking.getItem().getId())
                    ))
                    .collect(Collectors.toList());
        } else {
            return bookingRepository.findByItem_Owner_IdAndStatus(
                    ownerId, state, Sort.by(Sort.Direction.DESC, "start"))
                    .stream()
                    .map(booking -> bookingMapper.toDto(
                            booking,
                            userRepository.findUserById(booking.getBooker().getId()),
                            itemRepository.findItemById(booking.getItem().getId())
                    ))
                    .collect(Collectors.toList());
        }
    }
}