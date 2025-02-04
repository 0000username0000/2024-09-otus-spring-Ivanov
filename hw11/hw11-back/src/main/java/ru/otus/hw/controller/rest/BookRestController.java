package ru.otus.hw.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.dto.BookDtoService;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookRestController {

    private final BookService bookService;
    private final BookDtoService bookDtoService;

    @GetMapping
    public Flux<BookDto> getAllBooks() {
        return bookService.findAll()
                .flatMap(book -> bookDtoService.toDto(Mono.just(book)));
    }

    @PostMapping
    public Mono<Void> saveBook(@RequestBody BookDto bookDto) {
        return bookDtoService.toEntity(Mono.just(bookDto))
                .flatMap(book -> bookService.save(Mono.just(book)));
    }


    @PutMapping("/{id}")
    public Mono<Void> updateBook(@PathVariable Long id, @RequestBody BookDto bookDto) {
        return bookService.findByIdNN(id)
                .flatMap(existingBook -> {
                    existingBook.setTitle(bookDto.getTitle());
                    return bookService.save(Mono.just(existingBook));
                }).then();
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteBook(@PathVariable Long id) {
        return bookService.deleteById(id);
    }
}
