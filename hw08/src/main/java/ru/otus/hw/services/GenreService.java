package ru.otus.hw.services;

import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;


public interface GenreService {

    List<Genre> findAll();

    List<Genre> findAllByIds(Set<String> ids);

    void save(Genre genre);

    Genre findByIdNN(String id);
}
