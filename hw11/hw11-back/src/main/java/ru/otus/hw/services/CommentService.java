package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Comment;

public interface CommentService {

    Mono<Comment> findByIdNN(long id);

    Mono<Comment> save(Comment comment);

    Mono<Void> deleteById(long id);

    Flux<Comment> findByBookId(long bookId);
}
