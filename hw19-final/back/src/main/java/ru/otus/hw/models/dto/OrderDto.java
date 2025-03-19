package ru.otus.hw.models.dto;

import lombok.Data;
import ru.otus.hw.models.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
public class OrderDto {

    private UUID id;

    private LocalDateTime orderDate;

    private OrderStatus status;

    private double totalPrice;

    private UUID userId;

    private Set<OrderItemDto> orderItems;
}
