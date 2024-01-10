package ru.practicum.server.item.model;

import org.springframework.beans.factory.annotation.Value;

public interface ItemRequestProjection {

    Long getId();

    String getName();

    String getDescription();

    Boolean getAvailable();

    @Value("#{target.request.id}")
    long getRequestId();
}
