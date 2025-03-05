package ru.otus.hw.services;

import ru.otus.hw.models.Book;

import java.util.List;

public interface BookService {

    Book findByIdNN(long id);

    List<Book> findAll();

    void save(Book book);

    void deleteById(long id);
}
