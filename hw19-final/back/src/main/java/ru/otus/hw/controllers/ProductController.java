package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.models.dto.ProductDto;
import ru.otus.hw.models.interfaces.OnCreate;
import ru.otus.hw.models.interfaces.OnUpdate;
import ru.otus.hw.services.ProductService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "${crossOrigins}")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductDto> getAllProducts() {
        return productService.findAll().stream()
                .map(productService::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ProductDto getProductById(@PathVariable UUID id) {
        return productService.toDto(productService.findByIdNN(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto createProduct(@RequestBody @Validated(OnCreate.class) ProductDto productDto) {
        return productService.toDto(productService.save(productDto));
    }

    @PutMapping("/{id}")
    public ProductDto updateProduct(@PathVariable UUID id,
                                    @RequestBody @Validated(OnUpdate.class) ProductDto productDto) {
        productDto.setId(id);
        return productService.toDto(productService.save(productDto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable UUID id) {
        productService.deleteById(id);
    }

    @GetMapping("/category/{categoryId}")
    public List<ProductDto> getProductsByCategory(@PathVariable UUID categoryId) {
        return productService.findByCategoryId(categoryId).stream()
                .map(productService::toDto)
                .toList();
    }
}