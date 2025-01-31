package ru.otus.hw.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class AuthorServiceImpl implements AuthorService{
    private final AuthorRepository authorRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Transactional
    @Override
    public Author findByIdNN(long id) {
        return authorRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Author not found with id = %s", id)));
    }

    @Transactional
    @Override
    public Author save(Author author) {
        return authorRepository.save(author);
    }
}
