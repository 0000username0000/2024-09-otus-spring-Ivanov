package ru.otus.hw.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.GenreService;
import ru.otus.hw.services.dto.GenreDtoService;

import java.util.List;

@RestController
@RequestMapping("/api/genres")
@RequiredArgsConstructor
public class GenreRestController {

    private final GenreService genreService;
    private final GenreDtoService genreDtoService;

    @GetMapping
    public List<GenreDto> getAllGenres() {
        List<Genre> genres = genreService.findAll();
        return genreDtoService.toDtoList(genres);
    }
}
