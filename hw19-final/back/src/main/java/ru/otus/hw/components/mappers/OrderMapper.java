package ru.otus.hw.components.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.dto.OrderDto;
import ru.otus.hw.models.dto.OrderItemDto;
import ru.otus.hw.models.entities.Order;

import java.util.stream.Collectors;

@Component
public class OrderMapper {

    private final OrderItemMapper orderItemMapper;

    public OrderMapper(OrderItemMapper orderItemMapper) {
        this.orderItemMapper = orderItemMapper;
    }

    public OrderDto toDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setUserId(order.getUser().getId());
        dto.setOrderItems(order.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toSet()));
        return dto;
    }

    public Order toEntity(OrderDto dto) {
        Order order = new Order();
        order.setId(dto.getId());
        order.setOrderDate(dto.getOrderDate());
        order.setStatus(dto.getStatus());
        order.setTotalPrice(dto.getTotalPrice());
        // Здесь нужно установить пользователя и элементы заказа, если они доступны
        return order;
    }
}
