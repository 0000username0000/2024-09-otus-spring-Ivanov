package ru.otus.hw.services;

import ru.otus.hw.models.entities.Product;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    List<Product> findAll();

    Product findByIdNN(UUID id);

    Product save(Product product);

    void deleteById(UUID id);

    List<Product> findByCategoryId(UUID uuid);
}