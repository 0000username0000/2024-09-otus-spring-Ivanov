package ru.otus.hw.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.BooksGenresRepository;
import ru.otus.hw.repositories.BookRepository;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService{

    private final BookRepository bookRepository;

    private final BooksGenresRepository booksGenresRepository;

    @Transactional
    @Override
    public Mono<Book> findByIdNN(long id) {
        return bookRepository.findById(id).switchIfEmpty(Mono.error(() ->
                new EntityNotFoundException(String.format("Book not found with id = %s", id))));
    }

    @Transactional
    @Override
    public Flux<Book> findAll() {
        return bookRepository.findAll();
    }

    @Transactional
    @Override
    public Mono<Book> save(Mono<Book> book) {
        return book.flatMap(bookRepository::save);
    }

    @Transactional
    @Override
    public Mono<Void> deleteById(long bookId) {
        return booksGenresRepository.deleteByBookId(bookId)
                .then(bookRepository.deleteById(bookId));
    }
}

