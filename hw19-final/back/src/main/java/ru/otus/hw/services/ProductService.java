package ru.otus.hw.services;

import ru.otus.hw.models.dto.ProductDto;
import ru.otus.hw.models.entities.Product;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    List<Product> findAll();

    Product findByIdNN(UUID id);

    Product save(ProductDto productDto);

    void deleteById(UUID id);

    List<Product> findByCategoryId(UUID uuid);

    void reduceQuantity(UUID productId, int quantity);

    void increaseQuantity(UUID productId, int quantity);

    ProductDto toDto(Product product);
}