package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.entities.Order;
import ru.otus.hw.repositories.OrderRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Order findByIdNN(UUID id) {
        return orderRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Order not found with id = %s", id)));
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public void deleteById(UUID id) {
        orderRepository.deleteById(id);
    }
}