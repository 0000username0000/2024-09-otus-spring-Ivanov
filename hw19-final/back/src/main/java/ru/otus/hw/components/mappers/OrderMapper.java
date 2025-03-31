package ru.otus.hw.components.mappers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.dto.OrderDto;
import ru.otus.hw.models.entities.Order;
import ru.otus.hw.models.entities.OrderItem;
import ru.otus.hw.models.entities.User;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class OrderMapper {

    private final OrderItemMapper orderItemMapper;


    public OrderDto toDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        System.out.println(order.getId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setUserId(order.getUser().getId());
//        if (Objects.nonNull(order.getUser())) {
//            dto.(order.getUser().getLogin());
//        }
        if (order.getOrderItems() != null) {
            dto.setOrderItems(order.getOrderItems().stream()
                    .map(orderItemMapper::toDto)
                    .collect(Collectors.toSet()));
        }
        return dto;
    }

    public Order toEntity(OrderDto dto, User user) {
        Order order = new Order();
        order.setId(dto.getId());
        order.setOrderNumber(dto.getOrderNumber());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(dto.getStatus());
        order.setTotalPrice(dto.getTotalPrice());
        order.setUser(user);
        if (dto.getOrderItems() != null) {
            Set<OrderItem> items = dto.getOrderItems().stream()
                    .map(orderItemMapper::toEntity)
                    .peek(item -> item.setOrder(order))
                    .collect(Collectors.toSet());
            order.setOrderItems(items);
        }
        return order;
    }

}