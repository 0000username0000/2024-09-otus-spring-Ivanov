package ru.otus.hw.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.services.dto.BookDtoService;
import ru.otus.hw.services.BookServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookRestController {

    private final BookServiceImpl bookServiceImpl;

    private final BookDtoService bookDtoService;

    @GetMapping
    public List<BookDto> getAllBooks() {
        List<Book> books = bookServiceImpl.findAll();
        return bookDtoService.toDtoList(books);
    }

    @PostMapping
    public BookDto saveBook(@RequestBody BookDto bookDto) {
        Book book = bookDtoService.toEntity(bookDto);
        bookServiceImpl.save(book);
        return bookDtoService.toDto(book);
    }

    @PutMapping("/{id}")
    public BookDto updateBook(@PathVariable Long id, @RequestBody BookDto bookDto) {
        Book book = bookDtoService.toEntity(bookDto);
        book.setId(id);
        bookServiceImpl.save(book);
        return bookDtoService.toDto(book);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookServiceImpl.deleteById(id);
    }
}