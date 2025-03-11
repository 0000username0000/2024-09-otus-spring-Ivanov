package ru.otus.hw.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service(value = AuthorService.NAME)
@AllArgsConstructor
public class AuthorService {

    public static final String NAME = "authorService";

    private final AuthorRepository authorRepository;

    @Transactional(readOnly = true)
    @CircuitBreaker(name = NAME, fallbackMethod = "findAllFallback")
    @Retry(name = NAME, fallbackMethod = "findAllFallback")
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = NAME, fallbackMethod = "findByIdFallback")
    @Retry(name = NAME, fallbackMethod = "findByIdFallback")
    public Optional<Author> findById(long id) {
        return authorRepository.findById(id);
    }

    @Transactional
    @CircuitBreaker(name = NAME, fallbackMethod = "saveFallback")
    @Retry(name = NAME, fallbackMethod = "saveFallback")
    public Author save(Author author) {
        return authorRepository.save(author);
    }

    public List<Author> findAllFallback(Throwable t) {
        return Collections.emptyList();
    }

    public Optional<Author> findByIdFallback(long id, Throwable t) {
        return Optional.empty();
    }

    public Author saveFallback(Author author, Throwable t) {
        throw new RuntimeException("Fallback: Error saving author", t);
    }
}