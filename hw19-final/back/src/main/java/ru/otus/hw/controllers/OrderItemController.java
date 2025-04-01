package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.components.mappers.OrderItemMapper;
import ru.otus.hw.models.dto.OrderItemDto;
import ru.otus.hw.models.entities.OrderItem;
import ru.otus.hw.services.OrderItemService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/order-items")
@RequiredArgsConstructor
@CrossOrigin(origins = "${crossOrigins}")
public class OrderItemController {

    private final OrderItemService orderItemService;

    private final OrderItemMapper orderItemMapper;

    @GetMapping
    public List<OrderItemDto> getAllOrderItems() {
        return orderItemService.findAll().stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public OrderItemDto getOrderItemById(@PathVariable UUID id) {
        OrderItem orderItem = orderItemService.findByIdNN(id);
        return orderItemMapper.toDto(orderItem);
    }

    @PostMapping
    public OrderItemDto createOrderItem(@RequestBody OrderItemDto orderItemDto) {
        OrderItem orderItem = orderItemMapper.toEntity(orderItemDto);
        OrderItem savedOrderItem = orderItemService.save(orderItem);
        return orderItemMapper.toDto(savedOrderItem);
    }

    @PutMapping("/{id}")
    public OrderItemDto updateOrderItem(@PathVariable UUID id, @RequestBody OrderItemDto orderItemDto) {
        orderItemDto.setId(id);
        OrderItem orderItem = orderItemMapper.toEntity(orderItemDto);
        OrderItem updatedOrderItem = orderItemService.save(orderItem);
        return orderItemMapper.toDto(updatedOrderItem);
    }

    @DeleteMapping("/{id}")
    public void deleteOrderItem(@PathVariable UUID id) {
        orderItemService.deleteById(id);
    }
}