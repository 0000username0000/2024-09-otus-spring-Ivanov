package ru.otus.hw.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.JpaGenreRepository;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class GenreService {

    private final JpaGenreRepository genreRepository;

    @Transactional(readOnly = true)
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Genre> findAllByIds(Set<Long> ids) {
        return genreRepository.findAllByIds(ids);
    }

    @Transactional
    public Genre save(Genre genre) {
        return genreRepository.save(genre);
    }
}
