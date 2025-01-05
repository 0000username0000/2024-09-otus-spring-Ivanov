package ru.otus.hw.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public Optional<Comment>findById(String id) {
        return commentRepository.findById(id);
    }

    @Transactional
    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    @Transactional
    public void deleteById(String id) {
        commentRepository.deleteById(id);
    }

    @Transactional
    public List<Comment> findByBookId(String bookId) {
        return commentRepository.findByBookId(bookId);
    }
}
