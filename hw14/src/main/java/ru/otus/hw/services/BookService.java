package ru.otus.hw.services;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.BookRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject, 'READ')")
    public Optional<Book> findById(long id) {
        return bookRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject, 'READ')")
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Transactional
    @PreAuthorize("hasPermission(#noticeMessage, 'WRITE')")
    public void save(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    @PreAuthorize("hasPermission(#noticeMessage, 'WRITE')")
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }
}
