package ru.otus.hw.controller.rest;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.dto.AuthorDtoService;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthorRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthorService authorService;

    @Mock
    private AuthorDtoService authorDtoService;

    @InjectMocks
    private AuthorRestController authorRestController;

    public AuthorRestControllerTest() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authorRestController).build();
    }

    @Test
    void testGetAllAuthors() throws Exception {
        AuthorDto authorDto = new AuthorDto(1L, "John Doe");
        when(authorService.findAll()).thenReturn(Collections.singletonList(new Author()));
        when(authorDtoService.toDtoList(anyList())).thenReturn(Collections.singletonList(authorDto));

        mockMvc.perform(get("/api/authors")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].fullName").value("John Doe"));

        verify(authorService, times(1)).findAll();
        verify(authorDtoService, times(1)).toDtoList(anyList());
    }
}
