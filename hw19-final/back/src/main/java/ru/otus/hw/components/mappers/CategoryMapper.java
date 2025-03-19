package ru.otus.hw.components.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.dto.CategoryDto;
import ru.otus.hw.models.entities.Category;

@Component
public class CategoryMapper {

    public CategoryDto toDto(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        return dto;
    }

    public Category toEntity(CategoryDto dto) {
        Category category = new Category();
        category.setId(dto.getId());
        category.setName(dto.getName());
        return category;
    }
}