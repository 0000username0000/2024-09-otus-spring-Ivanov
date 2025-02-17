package ru.otus.hw.controller.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.mapper.dto.BookDtoMapper;
import ru.otus.hw.models.Book;
import ru.otus.hw.services.BookService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebMvcTest(BookRestController.class)
public class BookRestControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private BookService bookService;

    @MockBean
    private BookDtoMapper bookDtoMapper;

    @Test
    void shouldGetAllBooks() {
        Book book1 = new Book(1L, "title1", null, null);
        Book book2 = new Book(2L, "title2", null, null);

        BookDto bookDto1 = new BookDto(1L, "title1");
        BookDto bookDto2 = new BookDto(2L, "title2");

        when(bookService.findAll()).thenReturn(List.of(book1, book2));
        when(bookDtoMapper.toDto(book1)).thenReturn(bookDto1);
        when(bookDtoMapper.toDto(book2)).thenReturn(bookDto2);

        webTestClient.get().uri("/api/books")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookDto.class)
                .hasSize(2)
                .contains(bookDto1, bookDto2);
    }

    @Test
    void shouldSaveBook() {
        BookDto bookDto = new BookDto(null, "title");
        Book book = new Book(3L, "title", null, null);
        BookDto savedBookDto = new BookDto(3L, "title");

        when(bookDtoMapper.toEntity(any())).thenReturn(book);
        when(bookService.save(any())).thenReturn(book);
        when(bookDtoMapper.toDto(any())).thenReturn(savedBookDto);

        webTestClient.post().uri("/api/books")
                .bodyValue(bookDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookDto.class)
                .isEqualTo(savedBookDto);
    }

    @Test
    void shouldDeleteBook() {
        Long bookId = 1L;
        doNothing().when(bookService).deleteById(bookId);

        webTestClient.delete().uri("/api/books/" + bookId)
                .exchange()
                .expectStatus().isOk();

        verify(bookService, times(1)).deleteById(bookId);
    }
}
