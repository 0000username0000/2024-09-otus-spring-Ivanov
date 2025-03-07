package ru.otus.hw.controller.rest;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.services.BookServiceImpl;
import ru.otus.hw.mapper.dto.BookDtoMapper;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BookRestControllerTest {

    private final MockMvc mockMvc;

    @Mock
    private BookServiceImpl bookServiceImpl;

    @Mock
    private BookDtoMapper bookDtoMapper;

    @InjectMocks
    private BookRestController bookRestController;

    public BookRestControllerTest() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookRestController).build();
    }

    @Test
    void testGetAllBooks() throws Exception {
        BookDto bookDto = new BookDto(1L, "Book Title");
        when(bookServiceImpl.findAll()).thenReturn(Collections.singletonList(new Book()));
        when(bookDtoMapper.toDtoList(anyList())).thenReturn(Collections.singletonList(bookDto));

        mockMvc.perform(get("/api/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Book Title"));

        verify(bookServiceImpl, times(1)).findAll();
        verify(bookDtoMapper, times(1)).toDtoList(anyList());
    }

    @Test
    void testSaveBook() throws Exception {
        BookDto inputDto = new BookDto(null, "New Book");
        Book book = new Book();
        BookDto savedDto = new BookDto(1L, "New Book");

        when(bookDtoMapper.toEntity(inputDto)).thenReturn(book);

        doNothing().when(bookServiceImpl).save(book);

        when(bookDtoMapper.toDto(book)).thenReturn(savedDto);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"New Book\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("New Book"));

        verify(bookServiceImpl, times(1)).save(book);
        verify(bookDtoMapper, times(1)).toDto(book);
    }

    @Test
    void testDeleteBook() throws Exception {
        doNothing().when(bookServiceImpl).deleteById(1L);

        mockMvc.perform(delete("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(bookServiceImpl, times(1)).deleteById(1L);
    }
}
