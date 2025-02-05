package ru.otus.hw.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.GenreRepository;

import java.util.Set;

@Service
@AllArgsConstructor
public class GenreServiceImpl implements GenreService{


    private final GenreRepository genreRepository;

    @Transactional
    @Override
    public Flux<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Transactional
    @Override
    public Flux<Genre> findAllByIds(Set<Long> ids) {
        return genreRepository.findByIdIn(ids);
    }

    @Transactional
    @Override
    public Mono<Void> save(Genre genre) {
        return genreRepository.save(genre).then();
    }
}
