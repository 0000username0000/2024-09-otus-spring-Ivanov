package ru.otus.hw.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.entities.Order;
import ru.otus.hw.models.entities.OrderItem;
import ru.otus.hw.models.entities.Product;
import ru.otus.hw.repositories.OrderRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final ProductService productService;

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
    @Transactional
    public Order save(Order order) {
        order.getOrderItems().forEach(e ->
                productService.reduceQuantity(e.getProduct().getId(), e.getQuantity()));
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        findByIdNN(id).getOrderItems().forEach(e ->
                productService.increaseQuantity(e.getProduct().getId(), e.getQuantity()));
        orderRepository.deleteById(id);
    }
}