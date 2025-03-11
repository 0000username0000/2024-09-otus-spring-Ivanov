package ru.otus.hw.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.models.Book;
import ru.otus.hw.services.BookService;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    void shouldReturnBooksList() throws Exception {
        Mockito.when(bookService.findAll()).thenReturn(Arrays.asList(
                new Book(1L, "Book 1", null, null),
                new Book(2L, "Book 2", null, null)
        ));
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(view().name("books"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", hasSize(2)))
                .andExpect(model().attribute("books", hasItem(
                        hasProperty("title", is("Book 1"))
                )));
    }

    @Test
    void shouldReturnEditPageForBook() throws Exception {
        Mockito.when(bookService.findById(1L)).thenReturn(Optional.of(new Book(1L, "Book 1", null, null)));
        mockMvc.perform(get("/book-edit").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("book-editor"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attribute("book", hasProperty("title", is("Book 1"))));
    }

    @Test
    void shouldRedirectAfterSavingEditedBook() throws Exception {
        mockMvc.perform(post("/book-edit")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", "1")
                        .param("title", "Updated Book"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));
        Mockito.verify(bookService).save(any(Book.class));
    }

    @Test
    void shouldReturnCreateBookPage() throws Exception {
        mockMvc.perform(get("/book-create"))
                .andExpect(status().isOk())
                .andExpect(view().name("book-create"))
                .andExpect(model().attributeExists("book"));
    }

    @Test
    void shouldRedirectAfterSavingNewBook() throws Exception {
        mockMvc.perform(post("/book-create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", "New Book"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));
        Mockito.verify(bookService).save(any(Book.class));
    }

    @Test
    void shouldRedirectAfterDeletingBook() throws Exception {
        mockMvc.perform(get("/delete").param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));
        Mockito.verify(bookService).deleteById(eq(1L));
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenBookNotFound() throws Exception {
        Mockito.when(bookService.findById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/book-edit").param("id", "1"))
                .andExpect(status().isNotFound());
    }
}
