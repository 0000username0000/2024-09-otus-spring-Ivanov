package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.entities.Category;
import ru.otus.hw.repositories.CategoryRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Category findByIdNN(UUID id) {
        return categoryRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Category not found with id = %s", id)));
    }

    @Override
    @Transactional
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        categoryRepository.deleteById(id);
    }
}