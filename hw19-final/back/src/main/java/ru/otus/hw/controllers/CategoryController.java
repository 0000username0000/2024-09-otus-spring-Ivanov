package ru.otus.hw.controllers;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.components.mappers.CategoryMapper;
import ru.otus.hw.models.dto.CategoryDto;
import ru.otus.hw.models.entities.Category;
import ru.otus.hw.models.interfaces.OnCreate;
import ru.otus.hw.models.interfaces.OnUpdate;
import ru.otus.hw.services.CategoryService;

import java.util.List;
import java.util.UUID;

@Slf4j
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
                .toList();
    }

    @GetMapping("/{id}")
    public CategoryDto getCategoryById(@PathVariable UUID id) {
        return categoryMapper.toDto(categoryService.findByIdNN(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@RequestBody @Validated(OnCreate.class) CategoryDto categoryDto) {
        log.info("Creating category with name: {}", categoryDto.getName());
        Category category = new Category();
        category.setName(categoryDto.getName());
        return categoryMapper.toDto(categoryService.save(category));
    }

    @PutMapping("/{id}")
    public CategoryDto updateCategory(
            @PathVariable UUID id,
            @RequestBody @Validated(OnUpdate.class) CategoryDto categoryDto
    ) {
        Category category = categoryService.findByIdNN(id);
        category.setName(categoryDto.getName());
        return categoryMapper.toDto(categoryService.save(category));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable UUID id) {
        categoryService.deleteById(id);
    }
}