package ru.otus.hw.controller.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.dto.BookDtoService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(BookRestController.class)
public class BookRestControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private BookService bookService;

    @MockBean
    private BookDtoService bookDtoService;

    @Test
    void shouldGetAllBooks() {
        Book book1 = new Book(1L, "title1", 1L);
        Book book2 = new Book(2L, "title2", 2L);

        BookDto bookDto1 = new BookDto(1L, "title1");
        BookDto bookDto2 = new BookDto(2L, "title2");

        when(bookService.findAll()).thenReturn(Flux.just(book1, book2));
        when(bookDtoService.toDto(any())).thenReturn(Mono.just(bookDto1), Mono.just(bookDto2));

        webTestClient.get().uri("/api/books")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookDto.class)
                .hasSize(2)
                .contains(bookDto1, bookDto2);
    }

    @Test
    void shouldSaveBook() {
        BookDto bookDto = new BookDto(null, "Dune");
        Book book = new Book(3L, "Dune", 3L);

        when(bookDtoService.toEntity(any())).thenReturn(Mono.just(book));
        when(bookService.save(any())).thenReturn(Mono.just(book));

        webTestClient.post().uri("/api/books")
                .bodyValue(bookDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Book.class)
                .isEqualTo(book);
    }

    @Test
    void shouldDeleteBook() {
        when(bookService.deleteById(1L)).thenReturn(Mono.empty());

        webTestClient.delete().uri("/api/books/1")
                .exchange()
                .expectStatus().isOk();
    }
}
