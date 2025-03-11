package ru.otus.hw.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.GenreRepository;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service(value = GenreService.NAME)
@AllArgsConstructor
public class GenreService {

    public static final String NAME = "genreService";

    private final GenreRepository genreRepository;

    @Transactional(readOnly = true)
    @CircuitBreaker(name = NAME, fallbackMethod = "findAllFallback")
    @Retry(name = NAME, fallbackMethod = "findAllFallback")
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = NAME, fallbackMethod = "findAllByIdsFallback")
    @Retry(name = NAME, fallbackMethod = "findAllByIdsFallback")
    public List<Genre> findAllByIds(Set<Long> ids) {
        return genreRepository.findByIdIn(ids);
    }

    @Transactional
    @CircuitBreaker(name = NAME, fallbackMethod = "saveFallback")
    @Retry(name = NAME, fallbackMethod = "saveFallback")
    public void save(Genre genre) {
        genreRepository.save(genre);
    }

    public List<Genre> findAllFallback(Throwable t) {
        return Collections.emptyList();
    }

    public List<Genre> findAllByIdsFallback(Set<Long> ids, Throwable t) {
        return Collections.emptyList();
    }

    public void saveFallback(Genre genre, Throwable t) {
        throw new RuntimeException("Fallback: Error saving genre", t);
    }
}