package ru.otus.hw.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.exceptions.InsufficientStockException;
import ru.otus.hw.models.entities.Order;
import ru.otus.hw.models.entities.OrderItem;
import ru.otus.hw.repositories.OrderRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final ProductService productService;

    private final OrderItemService orderItemService;

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Order findByIdNN(UUID id) {
        return orderRepository.findById(id). orElseThrow(() ->
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

    @Override
    @Transactional
    public Order update(Order updatedOrder) {
        Order existingOrder = findByIdNN(updatedOrder.getId());
        processQuantityChanges(existingOrder, updatedOrder);
        addNewItems(existingOrder, updatedOrder);
        updateOrderDetails(existingOrder, updatedOrder);
        return orderRepository.save(existingOrder);
    }

    private void processQuantityChanges(Order existingOrder, Order updatedOrder) {
        for (OrderItem existingItem : existingOrder.getOrderItems()) {
            Optional<OrderItem> matchingUpdatedItem = findMatchingItem(updatedOrder, existingItem);
            if (matchingUpdatedItem.isPresent()) {
                handleQuantityAdjustment(existingItem, matchingUpdatedItem.get());
            } else {
                handleRemovedItem(existingItem);
            }
        }
    }

    private Optional<OrderItem> findMatchingItem(Order order, OrderItem itemToMatch) {
        return order.getOrderItems().stream()
                .filter(item -> item.getProduct().getId().equals(itemToMatch.getProduct().getId()))
                .findFirst();
    }

    private void handleQuantityAdjustment(OrderItem existingItem, OrderItem updatedItem) {
        int quantityDiff = existingItem.getQuantity() - updatedItem.getQuantity();
        UUID productId = existingItem.getProduct().getId();
        if (quantityDiff > 0) {
            productService.increaseQuantity(productId, quantityDiff);
        } else if (quantityDiff < 0) {
            productService.reduceQuantity(productId, -quantityDiff);
        }
    }

    private void handleRemovedItem(OrderItem removedItem) {
        productService.increaseQuantity(removedItem.getProduct().getId(), removedItem.getQuantity());
        orderItemService.deleteById(removedItem.getId());
    }

    private void addNewItems(Order existingOrder, Order updatedOrder) {
        for (OrderItem newItem : updatedOrder.getOrderItems()) {
            if (isNewItem(existingOrder, newItem)) {
                productService.reduceQuantity(newItem.getProduct().getId(), newItem.getQuantity());
            }
        }
    }

    private boolean isNewItem(Order existingOrder, OrderItem itemToCheck) {
        return existingOrder.getOrderItems().stream()
                .noneMatch(item -> item.getProduct().getId().equals(itemToCheck.getProduct().getId()));
    }

    private void updateOrderDetails(Order existingOrder, Order updatedOrder) {
        existingOrder.setOrderItems(updatedOrder.getOrderItems());
        existingOrder.setStatus(updatedOrder.getStatus());
        existingOrder.setTotalPrice(updatedOrder.getTotalPrice());
    }
}