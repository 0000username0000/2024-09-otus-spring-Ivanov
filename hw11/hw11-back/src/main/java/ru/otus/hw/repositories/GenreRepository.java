package ru.otus.hw.repositories;


import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import ru.otus.hw.models.Genre;

import java.util.Set;

public interface GenreRepository extends R2dbcRepository<Genre, Long> {

    Flux<Genre> findByIdIn(Set<Long> ids);

}
