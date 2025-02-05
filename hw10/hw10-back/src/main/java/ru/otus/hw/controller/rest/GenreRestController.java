package ru.otus.hw.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.GenreServiceImpl;
import ru.otus.hw.services.dto.GenreDtoService;

import java.util.List;

@RestController
@RequestMapping("/api/genres")
@RequiredArgsConstructor
public class GenreRestController {

    private final GenreServiceImpl genreServiceImpl;

    private final GenreDtoService genreDtoService;

    @GetMapping
    public List<GenreDto> getAllGenres() {
        List<Genre> genres = genreServiceImpl.findAll();
        return genreDtoService.toDtoList(genres);
    }
}
