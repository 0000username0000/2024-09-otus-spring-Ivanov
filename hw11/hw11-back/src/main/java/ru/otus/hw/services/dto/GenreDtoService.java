package ru.otus.hw.services.dto;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Genre;

@Service
public class GenreDtoService {

    public Mono<GenreDto> toDto(Mono<Genre> genreMono) {
        return genreMono.map(this::toDto);
    }

    public Flux<GenreDto> toDtoList(Flux<Genre> genresFlux) {
        return genresFlux.map(this::toDto);
    }

    private GenreDto toDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }
}
