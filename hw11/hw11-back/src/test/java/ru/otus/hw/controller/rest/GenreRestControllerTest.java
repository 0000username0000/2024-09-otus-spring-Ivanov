package ru.otus.hw.controller.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.mapper.dto.GenreDtoMapper;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.GenreService;

import java.util.List;

import static org.mockito.Mockito.when;

@WebFluxTest(GenreRestController.class)
public class GenreRestControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GenreService genreService;

    @MockBean
    private GenreDtoMapper genreDtoMapper;

    @Test
    void shouldGetAllGenres() {
        Genre genre1 = new Genre(1L, "genre1");
        Genre genre2 = new Genre(2L, "genre2");

        GenreDto genreDto1 = new GenreDto(1L, "genre1");
        GenreDto genreDto2 = new GenreDto(2L, "genre2");

        when(genreService.findAll()).thenReturn(List.of(genre1, genre2));
        when(genreDtoMapper.toDto(genre1)).thenReturn(genreDto1);
        when(genreDtoMapper.toDto(genre2)).thenReturn(genreDto2);

        webTestClient.get().uri("/api/genres")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(GenreDto.class)
                .hasSize(2)
                .contains(genreDto1, genreDto2);
    }
}

