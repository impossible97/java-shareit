package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryItemStorage implements ItemStorage {

    private final Map<Long, Item> items = new HashMap<>();
    private Long generatedId = 0L;

    @Override
    public Item addItem(Item item) {
        item.setId(++generatedId);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item updateItem(long itemId, Item updatedItem) {
        Item item = items.get(itemId);
        if (updatedItem.getName() != null) {
            item.setName(updatedItem.getName());
        }
        if (updatedItem.getDescription() != null) {
            item.setDescription(updatedItem.getDescription());
        }
        if (updatedItem.getAvailable() != null) {
            item.setAvailable(updatedItem.getAvailable());
        }
        items.put(item.getId(), item);

        return item;
    }

    @Override
    public Item getItem(long itemId) {
        return items.get(itemId);
    }

    @Override
    public List<Item> getAllItems(long userId) {
        ArrayList<Item> itemsByUser = new ArrayList<>(items.values());

        return itemsByUser.stream()
                .filter(item -> item.getOwner().getId() == userId)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<Item> searchItem(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        ArrayList<Item> itemsForSearch = new ArrayList<>(items.values());

        return itemsForSearch.stream()
                .filter(item -> (item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                        && item.getAvailable())
                .collect(Collectors.toList());
    }

    @Override
    public Set<Long> getItemsIds() {
        return new HashSet<>(items.keySet());
    }
}