package ru.otus.hw.controller.rest;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.GenreServiceImpl;
import ru.otus.hw.services.dto.GenreDtoService;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class GenreRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GenreServiceImpl genreServiceImpl;

    @Mock
    private GenreDtoService genreDtoService;

    @InjectMocks
    private GenreRestController genreRestController;

    public GenreRestControllerTest() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(genreRestController).build();
    }

    @Test
    void testGetAllGenres() throws Exception {
        GenreDto genreDto = new GenreDto(1L, "Fiction");
        when(genreServiceImpl.findAll()).thenReturn(Collections.singletonList(new Genre()));
        when(genreDtoService.toDtoList(anyList())).thenReturn(Collections.singletonList(genreDto));

        mockMvc.perform(get("/api/genres")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Fiction"));

        verify(genreServiceImpl, times(1)).findAll();
        verify(genreDtoService, times(1)).toDtoList(anyList());
    }
}
