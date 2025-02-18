package ru.otus.hw.services;

import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;

import java.util.List;

public interface CommentService {

    Comment findByIdNN(long id);

    void save(Comment comment);

    void deleteById(long id);

    List<Comment> findByBookId(long bookId);
}
