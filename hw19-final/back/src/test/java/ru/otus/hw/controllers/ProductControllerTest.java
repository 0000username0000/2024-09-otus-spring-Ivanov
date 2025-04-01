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
import ru.otus.hw.exceptions.GlobalExceptionHandler;
import ru.otus.hw.models.dto.ProductDto;
import ru.otus.hw.models.entities.Product;
import ru.otus.hw.services.ProductService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@Import({TestValidationConfig.class, GlobalExceptionHandler.class})
@DisplayName("Тестирование контроллера продуктов")
class ProductControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductService productService;

    private final UUID testId = UUID.randomUUID();
    private final UUID categoryId = UUID.randomUUID();
    private final ProductDto testDto = new ProductDto(
            testId,
            "Test Product",
            "Test Description",
            BigDecimal.valueOf(100.50),
            10,
            categoryId
    );

    @Test
    @DisplayName("Получение всех продуктов")
    void shouldReturnAllProducts() throws Exception {
        Mockito.when(productService.findAll())
                .thenReturn(List.of(new Product()));

        Mockito.when(productService.toDto(Mockito.any(Product.class)))
                .thenReturn(testDto);

        mvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(testId.toString())))
                .andExpect(jsonPath("$[0].name", is("Test Product")));
    }

    @Test
    @DisplayName("Получение продукта по ID")
    void shouldReturnProductById() throws Exception {
        Mockito.when(productService.findByIdNN(testId))
                .thenReturn(new Product());

        Mockito.when(productService.toDto(Mockito.any(Product.class)))
                .thenReturn(testDto);

        mvc.perform(get("/api/products/{id}", testId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testId.toString())))
                .andExpect(jsonPath("$.name", is("Test Product")));
    }

    @Test
    @DisplayName("Создание продукта")
    void shouldCreateProduct() throws Exception {
        Mockito.when(productService.save(Mockito.any(ProductDto.class)))
                .thenReturn(new Product());

        Mockito.when(productService.toDto(Mockito.any(Product.class)))
                .thenReturn(testDto);

        String requestBody = """
                {
                    "name": "Test Product",
                    "description": "Test Description",
                    "price": 100.50,
                    "quantity": 10,
                    "categoryId": "%s"
                }
                """.formatted(categoryId);

        mvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(testId.toString())));
    }

    @Test
    @DisplayName("Обновление продукта")
    void shouldUpdateProduct() throws Exception {
        Mockito.when(productService.save(Mockito.any(ProductDto.class)))
                .thenReturn(new Product());

        Mockito.when(productService.toDto(Mockito.any(Product.class)))
                .thenReturn(testDto);

        String requestBody = """
                {
                    "id": "%s",
                    "name": "Updated Product",
                    "description": "Updated Description",
                    "price": 150.75,
                    "quantity": 15,
                    "categoryId": "%s"
                }
                """.formatted(testId, categoryId);

        mvc.perform(put("/api/products/{id}", testId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Test Product")));
    }

    @Test
    @DisplayName("Удаление продукта")
    void shouldDeleteProduct() throws Exception {
        mvc.perform(delete("/api/products/{id}", testId))
                .andExpect(status().isNoContent());

        Mockito.verify(productService, Mockito.times(1)).deleteById(testId);
    }

    @Test
    @DisplayName("Получение продуктов по категории")
    void shouldGetProductsByCategory() throws Exception {
        Mockito.when(productService.findByCategoryId(categoryId))
                .thenReturn(List.of(new Product()));

        Mockito.when(productService.toDto(Mockito.any(Product.class)))
                .thenReturn(testDto);

        mvc.perform(get("/api/products/category/{categoryId}", categoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].categoryId", is(categoryId.toString())));
    }

//    @Test
//    @DisplayName("Валидация при создании - имя не должно быть пустым")
//    void shouldValidateCreateRequest() throws Exception {
//        String invalidRequestBody = """
//                {
//                    "name": "",
//                    "description": "Test",
//                    "price": 100,
//                    "quantity": 1,
//                    "categoryId": "%s"
//                }
//                """.formatted(categoryId);
//
//        mvc.perform(post("/api/products")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(invalidRequestBody))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.errors").exists())
//                .andExpect(jsonPath("$.errors[0].field").value("name"));
//    }
//
//    @Test
//    @DisplayName("Валидация при обновлении - цена не должна быть null")
//    void shouldValidateUpdateRequest() throws Exception {
//        String invalidRequestBody = """
//                {
//                    "id": "%s",
//                    "name": "Test",
//                    "description": "Test",
//                    "price": null,
//                    "quantity": 1,
//                    "categoryId": "%s"
//                }
//                """.formatted(testId, categoryId);
//
//        mvc.perform(put("/api/products/{id}", testId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(invalidRequestBody))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.errors").exists())
//                .andExpect(jsonPath("$.errors[0].field").value("price"));
//    }
}