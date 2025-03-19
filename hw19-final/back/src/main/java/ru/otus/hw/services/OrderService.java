package ru.otus.hw.services;

import ru.otus.hw.models.entities.Order;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    List<Order> findAll();

    Order findByIdNN(UUID id);

    Order save(Order order);

    void deleteById(UUID id);
}
