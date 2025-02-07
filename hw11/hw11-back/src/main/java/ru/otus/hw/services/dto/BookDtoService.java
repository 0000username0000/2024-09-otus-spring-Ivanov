package ru.otus.hw.services.dto;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Book;

@Service
public class BookDtoService {

    public Mono<BookDto> toDto(Mono<Book> bookMono) {
        return bookMono.map(this::toDto);
    }

    public Mono<Book> toEntity(Mono<BookDto> bookDtoMono) {
        return bookDtoMono.map(this::toEntity);
    }

    public Flux<BookDto> toDtoList(Flux<Book> booksFlux) {
        return booksFlux.map(this::toDto);
    }

    private BookDto toDto(Book book) {
        return new BookDto(book.getId(), book.getTitle());
    }

    private Book toEntity(BookDto bookDto) {
        Book book = new Book();
        book.setTitle(bookDto.getTitle());
        return book;
    }
}
