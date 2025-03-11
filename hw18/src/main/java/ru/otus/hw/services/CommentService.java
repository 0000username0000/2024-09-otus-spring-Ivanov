package ru.otus.hw.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.CommentRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service(value = CommentService.NAME)
@AllArgsConstructor
public class CommentService {

    public static final String NAME = "commentService";

    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    @CircuitBreaker(name = NAME, fallbackMethod = "findByIdFallback")
    @Retry(name = NAME, fallbackMethod = "findByIdFallback")
    public Optional<Comment> findById(long id) {
        return commentRepository.findById(id);
    }

    @Transactional
    @CircuitBreaker(name = NAME, fallbackMethod = "saveFallback")
    @Retry(name = NAME, fallbackMethod = "saveFallback")
    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    @Transactional
    @CircuitBreaker(name = NAME, fallbackMethod = "deleteByIdFallback")
    @Retry(name = NAME, fallbackMethod = "deleteByIdFallback")
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = NAME, fallbackMethod = "findByBookIdFallback")
    @Retry(name = NAME, fallbackMethod = "findByBookIdFallback")
    public List<Comment> findByBookId(long bookId) {
        return commentRepository.findByBookId(bookId);
    }

    public Optional<Comment> findByIdFallback(long id, Throwable t) {
        return Optional.empty();
    }

    public void saveFallback(Comment comment, Throwable t) {
        throw new RuntimeException("Fallback: Error saving comment", t);
    }

    public void deleteByIdFallback(long id, Throwable t) {
        throw new RuntimeException("Fallback: Error deleting comment with id: " + id, t);
    }

    public List<Comment> findByBookIdFallback(long bookId, Throwable t) {
        return Collections.emptyList();
    }
}