package ru.otus.hw.repositories;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.BooksGenres;
import ru.otus.hw.models.BooksGenresId;

public interface BooksGenresRepository extends R2dbcRepository<BooksGenres, BooksGenresId> {

    @Modifying
    @Query("DELETE FROM books_genres WHERE book_id = :bookId")
    Mono<Integer> deleteByBookId(long bookId);

}
