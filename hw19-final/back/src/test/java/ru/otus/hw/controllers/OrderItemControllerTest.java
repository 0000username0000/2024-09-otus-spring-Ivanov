package ru.otus.hw.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.config.TestValidationConfig;
import ru.otus.hw.components.mappers.OrderItemMapper;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.exceptions.GlobalExceptionHandler;
import ru.otus.hw.models.dto.OrderItemDto;
import ru.otus.hw.models.entities.Order;
import ru.otus.hw.models.entities.OrderItem;
import ru.otus.hw.models.entities.Product;
import ru.otus.hw.services.OrderItemService;
import ru.otus.hw.services.ProductService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderItemController.class)
@Import({TestValidationConfig.class, GlobalExceptionHandler.class})
@DisplayName("Тестирование контроллера элементов заказа")
class OrderItemControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private OrderItemService orderItemService;

    @MockBean
    private OrderItemMapper orderItemMapper;

    @MockBean
    private ProductService productService;

    private final UUID itemId = UUID.randomUUID();
    private final UUID orderId = UUID.randomUUID();
    private final UUID productId = UUID.randomUUID();
    private final String productName = "Test Product";

    private OrderItem createTestEntity() {
        OrderItem item = new OrderItem();
        item.setId(itemId);
        item.setQuantity(2);
        item.setPrice(BigDecimal.valueOf(100.50));

        Order order = new Order();
        order.setId(orderId);
        item.setOrder(order);

        Product product = new Product();
        product.setId(productId);
        product.setName(productName);
        item.setProduct(product);

        return item;
    }

    private OrderItemDto createTestDto() {
        OrderItemDto dto = new OrderItemDto();
        dto.setId(itemId);
        dto.setQuantity(2);
        dto.setPrice(BigDecimal.valueOf(100.50));
        dto.setOrderId(orderId);
        dto.setProductId(productId);
        dto.setProductName(productName);
        return dto;
    }

    @Test
    @DisplayName("Получение всех элементов заказа")
    void shouldGetAllOrderItems() throws Exception {
        OrderItem item = createTestEntity();
        OrderItemDto dto = createTestDto();

        Mockito.when(orderItemService.findAll())
                .thenReturn(List.of(item));
        Mockito.when(orderItemMapper.toDto(item))
                .thenReturn(dto);

        mvc.perform(get("/api/order-items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemId.toString())))
                .andExpect(jsonPath("$[0].productName", is(productName)));
    }

    @Test
    @DisplayName("Получение элемента заказа по ID")
    void shouldGetOrderItemById() throws Exception {
        OrderItem item = createTestEntity();
        OrderItemDto dto = createTestDto();

        Mockito.when(orderItemService.findByIdNN(itemId))
                .thenReturn(item);
        Mockito.when(orderItemMapper.toDto(item))
                .thenReturn(dto);

        mvc.perform(get("/api/order-items/{id}", itemId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemId.toString())))
                .andExpect(jsonPath("$.quantity", is(2)));
    }

    @Test
    @DisplayName("Создание элемента заказа")
    void shouldCreateOrderItem() throws Exception {
        OrderItem item = createTestEntity();
        OrderItemDto dto = createTestDto();
        OrderItemDto requestDto = createTestDto();
        requestDto.setId(null);

        Product product = new Product();
        product.setId(productId);

        Mockito.when(productService.findByIdNN(productId))
                .thenReturn(product);
        Mockito.when(orderItemMapper.toEntity(requestDto))
                .thenReturn(item);
        Mockito.when(orderItemService.save(item))
                .thenReturn(item);
        Mockito.when(orderItemMapper.toDto(item))
                .thenReturn(dto);

        String requestBody = """
                {
                    "quantity": 2,
                    "price": 100.50,
                    "orderId": "%s",
                    "productId": "%s"
                }
                """.formatted(orderId, productId);

        mvc.perform(post("/api/order-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(itemId.toString())));
    }

    @Test
    @DisplayName("Обновление элемента заказа")
    void shouldUpdateOrderItem() throws Exception {
        OrderItem item = createTestEntity();
        OrderItemDto dto = createTestDto();

        Product product = new Product();
        product.setId(productId);

        Mockito.when(productService.findByIdNN(productId))
                .thenReturn(product);
        Mockito.when(orderItemMapper.toEntity(dto))
                .thenReturn(item);
        Mockito.when(orderItemService.save(item))
                .thenReturn(item);
        Mockito.when(orderItemMapper.toDto(item))
                .thenReturn(dto);

        String requestBody = """
                {
                    "id": "%s",
                    "quantity": 2,
                    "price": 100.50,
                    "orderId": "%s",
                    "productId": "%s"
                }
                """.formatted(itemId, orderId, productId);

        mvc.perform(put("/api/order-items/{id}", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId", is(productId.toString())));
    }

    @Test
    @DisplayName("Удаление элемента заказа")
    void shouldDeleteOrderItem() throws Exception {
        mvc.perform(delete("/api/order-items/{id}", itemId))
                .andExpect(status().isNoContent());

        Mockito.verify(orderItemService, Mockito.times(1)).deleteById(itemId);
    }

    @Test
    @DisplayName("Валидация при создании - quantity не может быть меньше 1")
    void shouldValidateQuantityOnCreate() throws Exception {
        String invalidRequestBody = """
                {
                    "quantity": 0,
                    "price": 100.50,
                    "orderId": "%s",
                    "productId": "%s"
                }
                """.formatted(orderId, productId);

        mvc.perform(post("/api/order-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field").value("quantity"));
    }

    @Test
    @DisplayName("Валидация при обновлении - orderId обязателен")
    void shouldValidateOrderIdOnUpdate() throws Exception {
        String invalidRequestBody = """
                {
                    "id": "%s",
                    "quantity": 2,
                    "price": 100.50,
                    "productId": "%s"
                }
                """.formatted(itemId, productId);

        mvc.perform(put("/api/order-items/{id}", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field").value("orderId"));
    }

    @Test
    @DisplayName("Получение несуществующего элемента заказа")
    void shouldReturnNotFoundForNonExistingItem() throws Exception {
        Mockito.when(orderItemService.findByIdNN(itemId))
                .thenThrow(new EntityNotFoundException("OrderItem not found"));

        mvc.perform(get("/api/order-items/{id}", itemId))
                .andExpect(status().isNotFound());
    }
}