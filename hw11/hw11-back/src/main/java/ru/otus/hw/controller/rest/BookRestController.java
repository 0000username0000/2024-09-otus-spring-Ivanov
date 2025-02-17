package ru.otus.hw.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.services.BookService;
import ru.otus.hw.mapper.dto.BookDtoMapper;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookRestController {

    private final BookService bookService;

    private final BookDtoMapper bookDtoMapper;

    @GetMapping
    public Flux<BookDto> getAllBooks() {
        return Flux.defer(() -> Flux.fromIterable(bookService.findAll()))
                .map(bookDtoMapper::toDto);
    }

    @PostMapping
    public Mono<BookDto> saveBook(@RequestBody BookDto bookDto) {
        return Mono.fromCallable(() -> bookService.save(bookDtoMapper.toEntity(bookDto)))
                .map(bookDtoMapper::toDto);
    }

    @PutMapping("/{id}")
    public Mono<BookDto> updateBook(@PathVariable Long id, @RequestBody BookDto bookDto) {
        return Mono.fromCallable(() -> {
            Book existingBook = bookService.findByIdNN(id);
            existingBook.setTitle(bookDto.getTitle());
            return bookService.save(existingBook);
        }).map(bookDtoMapper::toDto);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteBook(@PathVariable Long id) {
        return Mono.fromRunnable(() -> bookService.deleteById(id)).then();
    }
}
