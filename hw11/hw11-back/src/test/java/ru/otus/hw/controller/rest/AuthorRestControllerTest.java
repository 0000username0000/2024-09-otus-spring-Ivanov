package ru.otus.hw.controller.rest;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.mapper.dto.AuthorDtoMapper;

import java.util.List;

import static org.mockito.Mockito.when;

@WebFluxTest(AuthorRestController.class)
public class AuthorRestControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private AuthorDtoMapper authorDtoMapper;

    @Test
    void shouldGetAllAuthors() {
        Author author1 = new Author(1L, "fio1");
        Author author2 = new Author(2L, "fio2");

        AuthorDto authorDto1 = new AuthorDto(1L, "fio1");
        AuthorDto authorDto2 = new AuthorDto(2L, "fio2");

        when(authorService.findAll()).thenReturn(List.of(author1, author2));
        when(authorDtoMapper.toDto(author1)).thenReturn(authorDto1);
        when(authorDtoMapper.toDto(author2)).thenReturn(authorDto2);

        webTestClient.get().uri("/api/authors")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AuthorDto.class)
                .hasSize(2)
                .contains(authorDto1, authorDto2);
    }
}
