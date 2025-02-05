package ru.otus.hw.services;

import ru.otus.hw.models.Comment;

import java.util.List;

public interface CommentService {

    Comment findByIdNN(long id);

    void save(Comment comment);

    void deleteById(long id);

    List<Comment> findByBookId(long bookId);
}
