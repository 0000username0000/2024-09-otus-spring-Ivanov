package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.entities.OrderItem;
import ru.otus.hw.repositories.OrderItemRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;

    @Override
    public List<OrderItem> findAll() {
        return orderItemRepository.findAll();
    }

    @Override
    public OrderItem findByIdNN(UUID id) {
        return orderItemRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("OrderItem not found with id = %s", id)));
    }

    @Override
    public OrderItem save(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    @Override
    public void deleteById(UUID id) {
        orderItemRepository.deleteById(id);
    }
}