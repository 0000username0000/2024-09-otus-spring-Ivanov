package ru.otus.hw.models.dto;

import lombok.Data;
import ru.otus.hw.models.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
public class OrderDto {

    private UUID id;

    private String orderNumber;

    private LocalDateTime orderDate;

    private OrderStatus status;

    private BigDecimal totalPrice;

    private UUID userId;

    private String userName;

    private Set<OrderItemDto> orderItems;
}
