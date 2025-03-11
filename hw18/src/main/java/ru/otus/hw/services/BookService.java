package ru.otus.hw.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.BookRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service(value = BookService.NAME)
@AllArgsConstructor
public class BookService {

    public static final String NAME = "bookService";

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    @CircuitBreaker(name = NAME, fallbackMethod = "findByIdFallback")
    @Retry(name = NAME, fallbackMethod = "findByIdFallback")
    public Optional<Book> findById(long id) {
        return bookRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = NAME, fallbackMethod = "findAllFallback")
    @Retry(name = NAME, fallbackMethod = "findAllFallback")
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Transactional
    @CircuitBreaker(name = NAME, fallbackMethod = "saveFallback")
    @Retry(name = NAME, fallbackMethod = "saveFallback")
    public void save(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    @CircuitBreaker(name = NAME, fallbackMethod = "deleteByIdFallback")
    @Retry(name = NAME, fallbackMethod = "deleteByIdFallback")
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    public Optional<Book> findByIdFallback(long id, Throwable t) {
        return Optional.empty();
    }

    public List<Book> findAllFallback(Throwable t) {
        return Collections.emptyList();
    }

    public void saveFallback(Book book, Throwable t) {
        throw new RuntimeException("Fallback: Error saving book", t);
    }

    public void deleteByIdFallback(long id, Throwable t) {
        throw new RuntimeException("Fallback: Error deleting book with id: " + id, t);
    }
}
