package ru.otus.hw.components.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.dto.CategoryDto;
import ru.otus.hw.models.entities.Category;

import java.util.Objects;

@Component
public class CategoryMapper {

    public CategoryDto toDto(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setProductCount(Objects.nonNull(category.getProducts()) ? category.getProducts().size() : 0);
        return dto;
    }

    public Category toEntity(CategoryDto dto) {
        Category category = new Category();
        if (Objects.nonNull(dto.getId())) {
            category.setId(dto.getId());
        }
        category.setId(dto.getId());
        category.setName(dto.getName());
        return category;
    }
}