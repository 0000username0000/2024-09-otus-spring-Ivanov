package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.components.mappers.OrderMapper;
import ru.otus.hw.models.dto.OrderDto;
import ru.otus.hw.models.entities.Order;
import ru.otus.hw.models.interfaces.OnCreate;
import ru.otus.hw.models.interfaces.OnUpdate;
import ru.otus.hw.services.OrderService;
import ru.otus.hw.services.UserService;

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

    private final UserService userService;

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
    public OrderDto createOrder(@RequestBody @Validated(OnCreate.class) OrderDto orderDto) {
        Order order = orderMapper.toEntity(orderDto, userService.findByIdNN(orderDto.getUserId()));
        Order savedOrder = orderService.save(order);
        return orderMapper.toDto(savedOrder);
    }

    @PutMapping("/{id}")
    public OrderDto updateOrder(@PathVariable UUID id, @RequestBody @Validated(OnUpdate.class) OrderDto orderDto) {
        orderDto.setId(id);
        Order order = orderMapper.toEntity(orderDto, userService.findByIdNN(orderDto.getUserId()));
        Order updatedOrder = orderService.save(order);
        return orderMapper.toDto(updatedOrder);
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable UUID id) {
        orderService.deleteById(id);
    }
}