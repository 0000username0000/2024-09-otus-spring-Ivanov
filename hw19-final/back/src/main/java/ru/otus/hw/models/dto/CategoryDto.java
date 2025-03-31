package ru.otus.hw.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import ru.otus.hw.models.interfaces.OnCreate;
import ru.otus.hw.models.interfaces.OnUpdate;

import java.util.UUID;

@Data
public class CategoryDto {

    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    private UUID id;

    @NotBlank(message = "Category name cannot be blank")
    private String name;

    @NotNull(message = "Product count ID cannot be null")
    private Integer productCount;
}