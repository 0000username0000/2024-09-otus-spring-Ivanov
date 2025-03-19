package ru.otus.hw.services;

import ru.otus.hw.models.entities.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    List<Category> findAll();

    Category findByIdNN(UUID id);

    Category save(Category category);

    void deleteById(UUID id);
}