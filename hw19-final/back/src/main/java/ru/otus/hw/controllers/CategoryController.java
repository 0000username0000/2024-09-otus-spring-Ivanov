package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.models.dto.CategoryDto;
import ru.otus.hw.models.entities.Category;
import ru.otus.hw.services.CategoryService;
import ru.otus.hw.components.mappers.CategoryMapper;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@CrossOrigin(origins = "${crossOrigins}")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping
    public List<CategoryDto> getAllCategories() {
        return categoryService.findAll().stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public CategoryDto getCategoryById(@PathVariable UUID id) {
        Category category = categoryService.findByIdNN(id);
        return categoryMapper.toDto(category);
    }

    @PostMapping
    public CategoryDto createCategory(@RequestParam String name) {
        Category category = new Category();
        category.setName(name);
        Category savedCategory = categoryService.save(category);
        return categoryMapper.toDto(savedCategory);
    }

    @PutMapping("/{id}")
    public CategoryDto updateCategory(@PathVariable UUID id, @RequestParam String name) {
        Category category = categoryService.findByIdNN(id);
        category.setName(name);
        Category updatedCategory = categoryService.save(category);
        return categoryMapper.toDto(updatedCategory);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable UUID id) {
        categoryService.deleteById(id);
    }
}