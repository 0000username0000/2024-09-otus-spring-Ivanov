package ru.otus.hw.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class JpaCommentService implements CommentRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Transactional(readOnly = true)
    @Override
    public Comment findById(long id) {
        return entityManager.find(Comment.class, id);
    }

    @Transactional
    @Override
    public Comment save(Comment comment) {
        if (comment.getId() == 0) {
            entityManager.persist(comment);
            return comment;
        }
        return entityManager.merge(comment);
    }

    @Override
    public void deleteById(long id) {
        entityManager.remove(findById(id));
    }

    @Override
    public List<Comment> findByBookId(long bookId) {
        return entityManager.createQuery("select e from Comment e where e.book.id = :bookId", Comment.class)
                .setParameter("bookId", bookId).getResultList();
    }
}
