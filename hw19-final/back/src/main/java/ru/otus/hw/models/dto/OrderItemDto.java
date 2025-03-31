package ru.otus.hw.models.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import ru.otus.hw.models.interfaces.OnCreate;
import ru.otus.hw.models.interfaces.OnUpdate;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class OrderItemDto {

    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    private UUID id;

    @Min(1)
    private int quantity;

    @NotNull
    private BigDecimal price;

    @NotNull(groups = OnUpdate.class)
    private UUID orderId;

    @NotNull
    private UUID productId;

    private String productName;
}
