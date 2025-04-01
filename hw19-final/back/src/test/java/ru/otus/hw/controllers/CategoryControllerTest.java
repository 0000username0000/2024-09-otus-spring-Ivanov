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
import ru.otus.hw.components.mappers.CategoryMapper;
import ru.otus.hw.config.TestValidationConfig;
import ru.otus.hw.config.ValidationConfig;
import ru.otus.hw.exceptions.GlobalExceptionHandler;
import ru.otus.hw.models.dto.CategoryDto;
import ru.otus.hw.models.entities.Category;
import ru.otus.hw.services.CategoryService;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
@Import({TestValidationConfig.class, GlobalExceptionHandler.class})
@DisplayName("Тестирование контроллера категорий")
class CategoryControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private CategoryMapper categoryMapper;

    private final UUID testId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    private final String testName = "Test Category";

    @Test
    @DisplayName("Обновление категории")
    void shouldUpdateCategory() throws Exception {
        Category existingCategory = new Category();
        existingCategory.setId(testId);
        existingCategory.setName("Old Name");

        Category updatedCategory = new Category();
        updatedCategory.setId(testId);
        updatedCategory.setName("Updated Name");

        CategoryDto responseDto = new CategoryDto();
        responseDto.setId(testId);
        responseDto.setName("Updated Name");

        Mockito.when(categoryService.findByIdNN(testId)).thenReturn(existingCategory);
        Mockito.when(categoryService.save(Mockito.any(Category.class))).thenReturn(updatedCategory);
        Mockito.when(categoryMapper.toDto(updatedCategory)).thenReturn(responseDto);

        mvc.perform(put("/api/categories/{id}", testId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"" + testId + "\",\"name\":\"Updated Name\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testId.toString())))
                .andExpect(jsonPath("$.name", is("Updated Name")));
    }

//    @Test
//    @DisplayName("Валидация при создании - имя не должно быть пустым")
//    void shouldValidateCreateRequest() throws Exception {
//        mvc.perform(post("/api/categories")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"name\":\"\"}"))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.errors").exists())
//                .andExpect(jsonPath("$.errors[0].field").exists())
//                .andExpect(jsonPath("$.errors[0].message").exists());
//    }

    @Test
    @DisplayName("Получение всех категорий")
    void shouldReturnAllCategories() throws Exception {
        // Given
        Category category = new Category();
        category.setId(testId);
        category.setName(testName);

        CategoryDto dto = new CategoryDto();
        dto.setId(testId);
        dto.setName(testName);

        Mockito.when(categoryService.findAll()).thenReturn(List.of(category));
        Mockito.when(categoryMapper.toDto(category)).thenReturn(dto);

        // When & Then
        mvc.perform(get("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(testId.toString())))
                .andExpect(jsonPath("$[0].name", is(testName)));
    }

    @Test
    @DisplayName("Получение категории по ID")
    void shouldReturnCategoryById() throws Exception {
        // Given
        Category category = new Category();
        category.setId(testId);
        category.setName(testName);

        CategoryDto dto = new CategoryDto();
        dto.setId(testId);
        dto.setName(testName);

        Mockito.when(categoryService.findByIdNN(testId)).thenReturn(category);
        Mockito.when(categoryMapper.toDto(category)).thenReturn(dto);

        mvc.perform(get("/api/categories/{id}", testId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testId.toString())))
                .andExpect(jsonPath("$.name", is(testName)));
    }

    @Test
    @DisplayName("Создание новой категории")
    void shouldCreateCategory() throws Exception {
        CategoryDto requestDto = new CategoryDto();
        requestDto.setName(testName);

        Category savedCategory = new Category();
        savedCategory.setId(testId);
        savedCategory.setName(testName);

        CategoryDto responseDto = new CategoryDto();
        responseDto.setId(testId);
        responseDto.setName(testName);

        Mockito.when(categoryService.save(Mockito.any(Category.class))).thenReturn(savedCategory);
        Mockito.when(categoryMapper.toDto(savedCategory)).thenReturn(responseDto);

        mvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"" + testName + "\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(testId.toString())))
                .andExpect(jsonPath("$.name", is(testName)));
    }

    @Test
    @DisplayName("Удаление категории")
    void shouldDeleteCategory() throws Exception {
        Mockito.doNothing().when(categoryService).deleteById(testId);

        mvc.perform(delete("/api/categories/{id}", testId))
                .andExpect(status().isNoContent());

        Mockito.verify(categoryService, Mockito.times(1)).deleteById(testId);
    }

    @Test
    @DisplayName("Валидация при обновлении - ID не должен быть null")
    void shouldValidateUpdateRequest() throws Exception {
        mvc.perform(put("/api/categories/{id}", testId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":null,\"name\":\"Valid Name\"}"))
                .andExpect(status().isBadRequest());
    }
}