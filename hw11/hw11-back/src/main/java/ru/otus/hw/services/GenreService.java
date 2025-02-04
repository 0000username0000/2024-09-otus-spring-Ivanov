package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Genre;

import java.util.Set;

public interface GenreService {

     Flux<Genre> findAll();

     Flux<Genre> findAllByIds(Set<Long> ids);

    Mono<Void> save(Genre genre);
}
