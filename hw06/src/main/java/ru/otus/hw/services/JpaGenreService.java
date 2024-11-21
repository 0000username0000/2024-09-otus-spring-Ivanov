package ru.otus.hw.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
        return entityManager.createQuery("select e from Genre e order by e.name", Genre.class).getResultList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Genre> findAllByIds(Set<Long> ids) {
        return entityManager.createQuery("select e from Genre where e.id in :Ids", Genre.class)
                .setParameter("Ids", ids).getResultList();
    }
}
