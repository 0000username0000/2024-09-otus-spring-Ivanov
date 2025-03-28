package ru.otus.hw.models.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class OrderItemDto {

    private UUID id;

    private int quantity;

    private BigDecimal price;

    private UUID orderId;

    private UUID productId;

    private String productName;
}
