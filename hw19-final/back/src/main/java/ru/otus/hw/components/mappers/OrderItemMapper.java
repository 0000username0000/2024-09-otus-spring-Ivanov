package ru.otus.hw.components.mappers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.dto.OrderItemDto;
import ru.otus.hw.models.entities.OrderItem;
import ru.otus.hw.services.ProductService;

@Component
@AllArgsConstructor
public class OrderItemMapper {

    private final ProductService productService;

    public OrderItemDto toDto(OrderItem item) {
        OrderItemDto dto = new OrderItemDto();
        dto.setId(item.getId());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());
        dto.setOrderId(item.getOrder().getId());
        dto.setProductId(item.getProduct().getId());
        if (item.getProduct() != null) {
            dto.setProductName(item.getProduct().getName());
        }
        return dto;
    }

    public OrderItem toEntity(OrderItemDto dto) {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(dto.getId());
        orderItem.setQuantity(dto.getQuantity());
        orderItem.setProduct(productService.findByIdNN(dto.getProductId()));
        orderItem.setPrice(dto.getPrice());
        return orderItem;
    }
}
