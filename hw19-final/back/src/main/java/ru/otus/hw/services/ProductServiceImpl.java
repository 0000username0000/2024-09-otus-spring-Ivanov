package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.exceptions.InsufficientStockException;
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

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void reduceQuantity(UUID productId, int quantity) throws InsufficientStockException {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity to reduce must be positive");
        }
        Product product = findByIdNN(productId);
        if (product.getQuantity() < quantity) {
            throw new InsufficientStockException(
                    String.format("Insufficient stock for product %s (ID: %s). Available: %d, requested: %d",
                            product.getName(), productId, product.getQuantity(), quantity));
        }
        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void increaseQuantity(UUID productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity to increase must be positive");
        }
        Product product = findByIdNN(productId);
        product.setQuantity(product.getQuantity() + quantity);
        productRepository.save(product);
    }
}