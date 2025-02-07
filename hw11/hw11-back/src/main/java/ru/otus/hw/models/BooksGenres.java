package ru.otus.hw.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "BOOKS_GENRES")
public class BooksGenres {

    @Id
    private BooksGenresId id;

    @Column("book_id")
    private Long bookId;

    @Column("genre_id")
    private Long genreId;
}
