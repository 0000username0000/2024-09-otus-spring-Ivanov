package ru.otus.hw.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthorServiceImp implements AuthorService {
    private final AuthorRepository authorRepository;


    @Transactional(readOnly = true)
    @Override
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Transactional
    @Override
    public Optional<Author> findById(long id) {
        return authorRepository.findById(id);
    }

    @Transactional
    @Override
    public void save(Author author) {
        authorRepository.save(author);
    }
}