package ru.otus.hw.services;

import ru.otus.hw.models.Author;

import java.util.List;

public interface AuthorService {

    List<Author> findAll();

    Author findByIdNN(String id);

    void save(Author author);
}
