package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.components.mappers.ProductMapper;
import ru.otus.hw.models.dto.ProductDto;
import ru.otus.hw.models.entities.Product;
import ru.otus.hw.services.ProductService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "${crossOrigins}")
public class ProductController {

    private final ProductService productService;

    private final ProductMapper productMapper;

    @GetMapping
    public List<ProductDto> getAllProducts() {
        return productService.findAll().stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ProductDto getProductById(@PathVariable UUID id) {
        Product product = productService.findByIdNN(id);
        return productMapper.toDto(product);
    }

    @PostMapping
    public ProductDto createProduct(@RequestBody ProductDto productDto) {
        Product product = productMapper.toEntity(productDto);
        Product savedProduct = productService.save(product);
        return productMapper.toDto(savedProduct);
    }

    @PutMapping("/{id}")
    public ProductDto updateProduct(@PathVariable UUID id, @RequestBody ProductDto productDto) {
        productDto.setId(id);
        Product product = productMapper.toEntity(productDto);
        Product updatedProduct = productService.save(product);
        return productMapper.toDto(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable UUID id) {
        productService.deleteById(id);
    }

    @GetMapping("/category/{categoryId}")
    public List<ProductDto> getProductsByCategory(@PathVariable UUID categoryId) {
        return productService.findByCategoryId(categoryId).stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }
}