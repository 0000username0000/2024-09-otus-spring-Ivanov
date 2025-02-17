package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Book;

import java.util.List;

public interface BookService {

    Book findByIdNN(long id);

    List<Book> findAll();

    Book save(Book book);

    void deleteById(long id);
}
