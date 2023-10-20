package ru.practicum.shareit.validation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dao.ItemStorage;

@Component
@AllArgsConstructor
public class ItemValidator {

    private final ItemStorage itemStorage;

    public void validateId(long itemId) {
        if (!itemStorage.getItemsIds().contains(itemId)) {
            throw new NotFoundException("Вещь с таким id = " + itemId + " не найдена!");
        }
    }

    public void validateOwner(long itemId, long userId) {
        if (!(itemStorage.getItem(itemId).getOwner().getId() == userId)) {
            throw new NotFoundException("Вещь с таким id = " + itemId + " принадлежит другому пользователю!");
        }
    }

    public void validateSearchText(String text) {
        if (text == null) {
            throw new ValidationException();
        }
    }
}
