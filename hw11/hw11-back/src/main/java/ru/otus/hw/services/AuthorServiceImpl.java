package ru.otus.hw.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;

@Service
@AllArgsConstructor
public class AuthorServiceImpl implements AuthorService{
    private final AuthorRepository authorRepository;

    @Transactional
    @Override
    public Flux<Author> findAll() {
        return authorRepository.findAll();
    }

    @Transactional
    @Override
    public Mono<Author> findByIdNN(long id) {
        return authorRepository.findById(id).switchIfEmpty(Mono.error(() ->
                new EntityNotFoundException(String.format("Author not found with id = %s", id))));
    }

    @Transactional
    @Override
    public Mono<Author> save(Author author) {
        return authorRepository.save(author);
    }
}
