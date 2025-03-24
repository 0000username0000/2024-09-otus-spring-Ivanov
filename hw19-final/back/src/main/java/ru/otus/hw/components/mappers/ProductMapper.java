package ru.otus.hw.components.mappers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.dto.ProductDto;
import ru.otus.hw.models.entities.Product;
import ru.otus.hw.services.CategoryService;
import ru.otus.hw.services.ProductService;

@Component
@AllArgsConstructor
public class ProductMapper {

    private final CategoryService categoryService;

    public ProductDto toDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setQuantity(product.getQuantity());
        dto.setCategoryId(product.getCategory().getId());
        return dto;
    }

    public Product toEntity(ProductDto dto) {
        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());
        product.setCategory(categoryService.findByIdNN(dto.getCategoryId()));
        return product;
    }
}
