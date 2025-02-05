package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Book;

public interface BookService {

    Mono<Book> findByIdNN(long id);

    Flux<Book> findAll();

    Mono<Book> save(Mono<Book> book);

    Mono<Void> deleteById(long id);
}
