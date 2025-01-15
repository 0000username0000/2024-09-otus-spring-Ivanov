package ru.otus.hw.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.BookRepository;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class BookServiceImp implements BookService {

    private final BookRepository bookRepository;

    private final GenreServiceImp genreServiceImp;

    private final AuthorServiceImp authorServiceImp;

    @Transactional
    @Override
    public Book findById(String id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Book not found with id = %s", id)));
    }

    @Transactional
    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Transactional
    @Override
    public void save(Book book) {
        if (Objects.nonNull(book.getGenres()) && !book.getGenres().isEmpty()) {
            book.getGenres().forEach(e -> genreServiceImp.findByIdNN(e.getId()));
        }
        if (Objects.nonNull(book.getAuthor())) {
            authorServiceImp.findByIdNN(book.getAuthor().getId());
        }
        bookRepository.save(book);
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        bookRepository.deleteById(id);
    }
}
