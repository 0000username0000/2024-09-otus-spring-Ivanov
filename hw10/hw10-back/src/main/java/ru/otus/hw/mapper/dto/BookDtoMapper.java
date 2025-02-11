package ru.otus.hw.mapper.dto;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookDtoMapper {

    public BookDto toDto(Book book) {
        return new BookDto(book.getId(), book.getTitle());
    }

    public Book toEntity(BookDto bookDto) {
        Book book = new Book();
        book.setTitle(bookDto.getTitle());
        return book;
    }

    public List<BookDto> toDtoList(List<Book> books) {
        return books.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
