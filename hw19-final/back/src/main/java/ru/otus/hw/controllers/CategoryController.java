package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.entities.Category;
import ru.otus.hw.services.CategoryService;

import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public Flux<Category> getAllCategories() {
        return Flux.fromIterable(categoryService.findAll());
    }

    @GetMapping("/{id}")
    public Mono<Category> getCategoryById(@PathVariable UUID id) {
        return Mono.justOrEmpty(categoryService.findById(id));
    }

    @PostMapping
    public Mono<Category> createCategory(@RequestParam String name) {
        Category category = new Category();
        category.setName(name);
        return Mono.just(categoryService.save(category));
    }

    @PutMapping("/{id}")
    public Mono<Category> updateCategory(@PathVariable UUID id, @RequestParam String name) {
        return Mono.justOrEmpty(categoryService.findById(id))
                .map(category -> {
                    category.setName(name);
                    return categoryService.save(category);
                });
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteCategory(@PathVariable UUID id) {
        return Mono.fromRunnable(() -> categoryService.deleteById(id));
    }
}