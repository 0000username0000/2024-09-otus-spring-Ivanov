package ru.otus.hw.services;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.BookRepository;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class JpaBookService implements BookRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Transactional(readOnly = true)
    @Override
    public Optional<Book> findById(long id) {
        return Optional.ofNullable(entityManager.find(Book.class, id));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Book> findAll() {
        EntityGraph<?> entityGraph = entityManager.getEntityGraph("books-with-author-genres-comments");
        TypedQuery<Book> typedQuery = entityManager.createQuery("select e from Book e order by e.title", Book.class);
        return typedQuery.setHint(FETCH.getKey(), entityGraph).getResultList();
    }

    @Transactional
    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            entityManager.persist(book);
            return book;
        }
        return entityManager.merge(book);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        entityManager.remove(findById(id));
    }
}
