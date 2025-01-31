package ru.otus.hw.services;

import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;

public interface GenreService {

     List<Genre> findAll();

     List<Genre> findAllByIds(Set<Long> ids);

    void save(Genre genre);
}
