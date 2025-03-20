package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.entities.Product;
import ru.otus.hw.repositories.ProductRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Product findByIdNN(UUID id) {
        return productRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Product not found with id = %s", id)));
    }

    @Override
    @Transactional
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<Product> findByCategoryId(UUID uuid) {
        return productRepository.findByCategory_Id(uuid);
    }
}