package ru.practicum.server.request.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.practicum.server.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long>, PagingAndSortingRepository<ItemRequest, Long> {

    List<ItemRequest> findAllByUser_Id(long userId, Sort sort);
}
