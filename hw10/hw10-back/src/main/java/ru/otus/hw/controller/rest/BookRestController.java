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
import ru.otus.hw.services.BookService;
import ru.otus.hw.mapper.dto.BookDtoMapper;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookRestController {

    private final BookService bookService;

    private final BookDtoMapper bookDtoMapper;

    @GetMapping
    public List<BookDto> getAllBooks() {
        List<Book> books = bookService.findAll();
        return bookDtoMapper.toDtoList(books);
    }

    @PostMapping
    public BookDto saveBook(@RequestBody BookDto bookDto) {
        Book book = bookDtoMapper.toEntity(bookDto);
        bookService.save(book);
        return bookDtoMapper.toDto(book);
    }

    @PutMapping("/{id}")
    public BookDto updateBook(@PathVariable Long id, @RequestBody BookDto bookDto) {
        Book book = bookDtoMapper.toEntity(bookDto);
        book.setId(id);
        bookService.save(book);
        return bookDtoMapper.toDto(book);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);
    }
}