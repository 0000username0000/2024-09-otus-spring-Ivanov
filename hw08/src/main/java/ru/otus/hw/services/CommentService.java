package ru.otus.hw.services;

import ru.otus.hw.models.Comment;

import java.util.List;

public interface CommentService {

    Comment findByIdNN(String id);

    void save(Comment comment);

    void deleteById(String id);

    List<Comment> findByBookId(String bookId);
}

