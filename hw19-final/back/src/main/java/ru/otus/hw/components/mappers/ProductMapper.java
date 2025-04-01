package ru.otus.hw.components.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.dto.ProductDto;
import ru.otus.hw.models.entities.Product;
import ru.otus.hw.models.entities.Category;

@Component
public class ProductMapper {

    public ProductDto toDto(Product product) {
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity(),
                product.getCategory() != null ? product.getCategory().getId() : null
        );
    }

    public Product toEntity(ProductDto dto, Category category) {
        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());
        product.setCategory(category);
        return product;
    }
}
