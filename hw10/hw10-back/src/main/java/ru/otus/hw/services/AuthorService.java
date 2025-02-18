package ru.otus.hw.services;

import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;

import java.util.List;

public interface AuthorService {

    List<Author> findAll();

    Author findByIdNN(long id);

    Author save(Author author);
}
