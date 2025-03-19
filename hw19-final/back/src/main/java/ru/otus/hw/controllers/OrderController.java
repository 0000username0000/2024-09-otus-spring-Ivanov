package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.models.dto.OrderDto;
import ru.otus.hw.models.entities.Order;
import ru.otus.hw.services.OrderService;
import ru.otus.hw.components.mappers.OrderMapper;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "${crossOrigins}")
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @GetMapping
    public List<OrderDto> getAllOrders() {
        return orderService.findAll().stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public OrderDto getOrderById(@PathVariable UUID id) {
        Order order = orderService.findByIdNN(id);
        return orderMapper.toDto(order);
    }

    @PostMapping
    public OrderDto createOrder(@RequestBody OrderDto orderDto) {
        Order order = orderMapper.toEntity(orderDto);
        Order savedOrder = orderService.save(order);
        return orderMapper.toDto(savedOrder);
    }

    @PutMapping("/{id}")
    public OrderDto updateOrder(@PathVariable UUID id, @RequestBody OrderDto orderDto) {
        orderDto.setId(id);
        Order order = orderMapper.toEntity(orderDto);
        Order updatedOrder = orderService.save(order);
        return orderMapper.toDto(updatedOrder);
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable UUID id) {
        orderService.deleteById(id);
    }
}