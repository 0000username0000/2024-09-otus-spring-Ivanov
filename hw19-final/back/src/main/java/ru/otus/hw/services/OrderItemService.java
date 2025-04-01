package ru.otus.hw.services;

import ru.otus.hw.models.entities.OrderItem;

import java.util.List;
import java.util.UUID;

public interface OrderItemService {

    List<OrderItem> findAll();

    OrderItem findByIdNN(UUID id);

    OrderItem save(OrderItem orderItem);

    void deleteById(UUID id);
}
