package ru.otus.hw.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class JpaGenreService implements GenreRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Transactional(readOnly = true)
    @Override
    public List<Genre> findAll() {
        TypedQuery<Genre> typedQuery = entityManager
                .createQuery("select e from Genre e order by e.name", Genre.class);
        return typedQuery.getResultList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Genre> findAllByIds(Set<Long> ids) {
        TypedQuery<Genre> typedQuery = entityManager
                .createQuery("select e from Genre where e.id in :Ids", Genre.class);
        return typedQuery.setParameter("Ids", ids).getResultList();
    }

    @Transactional
    @Override
    public Genre save(Genre genre) {
        if (genre.getId() == 0) {
            entityManager.persist(genre);
            return genre;
        }
        return entityManager.merge(genre);
    }
}
