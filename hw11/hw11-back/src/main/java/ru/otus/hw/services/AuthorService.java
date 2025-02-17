package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Author;

import java.util.List;

public interface AuthorService {

    List<Author> findAll();

    Author findByIdNN(long id);

    Author save(Author author);
}
