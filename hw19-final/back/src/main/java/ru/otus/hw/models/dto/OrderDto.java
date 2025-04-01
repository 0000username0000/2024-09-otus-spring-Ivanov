package ru.otus.hw.models.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import ru.otus.hw.models.enums.OrderStatus;
import ru.otus.hw.models.interfaces.OnCreate;
import ru.otus.hw.models.interfaces.OnUpdate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
public class OrderDto {
    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    private UUID id;

    @Null(groups = OnCreate.class)
    @NotBlank(groups = OnUpdate.class)
    private String orderNumber;

    @NotNull(groups = OnCreate.class)
    private LocalDateTime orderDate;

    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    private OrderStatus status;

    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    private BigDecimal totalPrice;

    @NotNull(groups = OnCreate.class)
    private UUID userId;

    @NotEmpty(groups = {OnCreate.class, OnUpdate.class})
    private Set<@Valid OrderItemDto> orderItems;
}
