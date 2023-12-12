package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemProjection;
import ru.practicum.shareit.item.model.ItemRequestProjection;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT it FROM Item it WHERE LOWER(it.name) LIKE LOWER(concat('%', :text, '%')) " +
            "OR LOWER(it.description) LIKE LOWER(concat('%', :text, '%'))")
    List<Item> findByText(@Param("text") String text);

    ItemProjection findItemById(Long itemId);

    @Query("SELECT it FROM Item it WHERE it.request.id IS NOT NULL AND it.request.id = ?1")
    List<ItemRequestProjection> findAllByRequest_Id(long requestId);
}
