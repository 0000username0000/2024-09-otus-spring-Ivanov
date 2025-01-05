package ru.otus.hw.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.JpaAuthorRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class AuthorService {

    private final JpaAuthorRepository authorRepository;

    @Transactional(readOnly = true)
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Author findById(long id) {
        return authorRepository.findById(id).orElse(null);
    }

    @Transactional
    public Author save(Author author) {
        return authorRepository.save(author);
    }
}
