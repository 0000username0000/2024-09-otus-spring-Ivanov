package ru.otus.hw.repositories;

import ru.otus.hw.models.Comment;

import java.util.List;

public interface CommentRepository {

    Comment findById(long id);

    Comment save(Comment comment);

    void deleteById(long id);

    List<Comment> findByBookId(long bookId);
}
