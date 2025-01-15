package ru.otus.hw.services;

import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    Optional<Comment>findById(long id);

    void save(Comment comment);

    void deleteById(long id);

    List<Comment> findByBookId(long bookId);
}
