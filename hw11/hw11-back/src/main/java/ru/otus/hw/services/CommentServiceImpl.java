package ru.otus.hw.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.CommentRepository;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;

    @Transactional
    @Override
    public Mono<Comment> findByIdNN(long id) {
        return commentRepository.findById(id).switchIfEmpty(Mono.error(() ->
                new EntityNotFoundException(String.format("Book not found with id = %s", id))));
    }

    @Transactional
    @Override
    public Mono<Comment> save(Comment comment) {
       return commentRepository.save(comment);
    }

    @Transactional
    @Override
    public Mono<Void> deleteById(long id) {
        return commentRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Flux<Comment> findByBookId(long bookId) {
        return commentRepository.findByBookId(bookId);
    }
}
