package ru.otus.hw.components.mappers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.dto.OrderDto;
import ru.otus.hw.models.dto.OrderItemDto;
import ru.otus.hw.models.entities.Order;
import ru.otus.hw.models.entities.OrderItem;
import ru.otus.hw.services.UserService;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class OrderMapper {

    private final OrderItemMapper orderItemMapper;

    private final UserService userService;


    public OrderDto toDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setUserId(order.getUser().getId());
        dto.setUserName(order.getUser().getLastName());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setOrderItems(order.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toSet()));
        return dto;
    }

    public Order toEntity(OrderDto dto) {
        Order order = new Order();
        order.setId(dto.getId());
        order.setOrderDate(Objects.nonNull(dto.getOrderDate()) ? dto.getOrderDate() : LocalDateTime.now());
        order.setStatus(dto.getStatus());
        order.setTotalPrice(dto.getTotalPrice());

        order.setUser(userService.findByIdNN(dto.getUserId()));

        Set<OrderItem> orderItems = dto.getOrderItems().stream()
                .map(itemDto -> {
                    OrderItem item = orderItemMapper.toEntity(itemDto);
                    item.setOrder(order);
                    return item;
                })
                .collect(Collectors.toSet());

        order.setOrderItems(orderItems);
        return order;
    }
}
