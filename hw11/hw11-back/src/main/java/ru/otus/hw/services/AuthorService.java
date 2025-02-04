package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Author;

public interface AuthorService {

    Flux<Author> findAll();

    Mono<Author> findByIdNN(long id);

    Mono<Author> save(Author author);
}
