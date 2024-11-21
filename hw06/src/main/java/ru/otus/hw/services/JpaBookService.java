package ru.otus.hw.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.BookRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
        return entityManager.createQuery("select e from Book e order by e.title", Book.class).getResultList();
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
