package ru.otus.hw.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class GenreServiceImp implements GenreService {

    private final GenreRepository genreRepository;

    @Transactional
    @Override
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Transactional
    @Override
    public List<Genre> findAllByIds(Set<Long> ids) {
        return genreRepository.findByIdIn(ids);
    }

    @Transactional
    @Override
    public void save(Genre genre) {
        genreRepository.save(genre);
    }
}
