package ru.otus.hw.components.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.dto.OrderItemDto;
import ru.otus.hw.models.entities.OrderItem;

@Component
public class OrderItemMapper {

    public OrderItemDto toDto(OrderItem orderItem) {
        OrderItemDto dto = new OrderItemDto();
        dto.setId(orderItem.getId());
        dto.setQuantity(orderItem.getQuantity());
        dto.setPrice(orderItem.getPrice());
        dto.setOrderId(orderItem.getOrder().getId());
        dto.setProductId(orderItem.getProduct().getId());
        return dto;
    }

    public OrderItem toEntity(OrderItemDto dto) {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(dto.getId());
        orderItem.setQuantity(dto.getQuantity());
        orderItem.setPrice(dto.getPrice());
        return orderItem;
    }
}
