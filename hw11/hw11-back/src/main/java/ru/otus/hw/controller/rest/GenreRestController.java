package ru.otus.hw.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.mapper.dto.GenreDtoMapper;
import ru.otus.hw.services.GenreService;

@RestController
@RequestMapping("/api/genres")
@RequiredArgsConstructor
public class GenreRestController {

    private final GenreService genreService;

    private final GenreDtoMapper genreDtoMapper;

    @GetMapping
    public Flux<GenreDto> getAllGenres() {
        return Flux.defer(() -> Flux.fromIterable(genreService.findAll()))
                .map(genreDtoMapper::toDto);
    }
}
