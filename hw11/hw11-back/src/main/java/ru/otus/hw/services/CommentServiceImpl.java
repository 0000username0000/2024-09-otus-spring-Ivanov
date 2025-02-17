package ru.otus.hw.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Transactional
    @Override
    public Comment findByIdNN(long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Comment not found with id = %s", id)));
    }

    @Transactional
    @Override
    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

    @Transactional
    @Override
    public List<Comment> findByBookId(long bookId) {
        return commentRepository.findByBookId(bookId);
    }
}
