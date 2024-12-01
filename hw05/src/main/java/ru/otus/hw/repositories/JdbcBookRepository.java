package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Optional;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final GenreRepository genreRepository;

    private final JdbcOperations jdbc;

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Override
    public Optional<Book> findById(long id) {
        return Optional
                .ofNullable(namedParameterJdbcOperations.query(
                                    "SELECT b.id, b.title, b.author_id, a.full_name, bg.genre_id, g.name " +
                                            "FROM books b " +
                                            "INNER JOIN authors a ON a.id = b.author_id " +
                                            "INNER JOIN books_genres bg ON bg.book_id = b.id " +
                                            "INNER JOIN genres g ON g.id = bg.genre_id " +
                                            "WHERE b.id = :id", Collections.singletonMap("id", id),
                        new BookResultSetExtractor()));
    }

    @Override
    public List<Book> findAll() {
        var genres = genreRepository.findAll();
        var relations = getAllGenreRelations();
        var books = getAllBooksWithoutGenres();
        mergeBooksInfo(books, genres, relations);
        return books;
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        namedParameterJdbcOperations.update("DELETE FROM books WHERE id = :id", Collections.singletonMap("id", id));
    }

    private List<Book> getAllBooksWithoutGenres() {
        return jdbc.query("SELECT b.id, b.title, b.author_id, a.full_name " +
                                "FROM books b " +
                                "JOIN authors a ON a.id = b.author_id", new BookRowMapper());
    }

    private List<BookGenreRelation> getAllGenreRelations() {
        return jdbc.query("SELECT book_id, genre_id FROM books_genres", (rs, rowNum) -> {
            long bookId = rs.getLong("book_id");
            long genreId = rs.getLong("genre_id");
            return new BookGenreRelation(bookId, genreId);
        });
    }


    private void mergeBooksInfo(List<Book> booksWithoutGenres, List<Genre> genres,
                                List<BookGenreRelation> relations) {
        Map<Long, Genre> genreMap = genres.stream()
                .collect(Collectors.toMap(Genre::getId, Function.identity()));
        Map<Long, List<Long>> bookGenreRelations = createBookGenreRelationsMap(relations);
        booksWithoutGenres.forEach(book ->
                assignGenresToBook(book, bookGenreRelations.get(book.getId()), genreMap)
        );
    }

    private Map<Long, List<Long>> createBookGenreRelationsMap(List<BookGenreRelation> relations) {
        Map<Long, List<Long>> bookGenreRelations = new HashMap<>();
        relations.forEach(relation ->
                bookGenreRelations.computeIfAbsent(relation.bookId(), k -> new ArrayList<>())
                        .add(relation.genreId())
        );
        return bookGenreRelations;
    }

    private void assignGenresToBook(Book book, List<Long> genreIds, Map<Long, Genre> genreMap) {
        if (genreIds != null) {
            List<Genre> genreList = new ArrayList<>();
            genreIds.stream()
                    .map(genreMap::get)
                    .filter(Objects::nonNull)
                    .forEach(genreList::add);
            book.setGenres(genreList);
        }
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            var ps = connection.prepareStatement("INSERT INTO books (title, author_id) VALUES (?, ?)",
                    new String[]{"id"});
            ps.setString(1, book.getTitle());
            ps.setLong(2, book.getAuthor().getId());
            return ps;
        }, keyHolder);
        book.setId(keyHolder.getKeyAs(Long.class));
        batchInsertGenresRelationsFor(book);
        return book;
    }

    private Book update(Book book) {
        namedParameterJdbcOperations.update(
                "UPDATE books SET title = :title, author_id = :authorId WHERE id = :bookId",
                Map.of("title", book.getTitle(),
                        "authorId", book.getAuthor().getId(),
                        "bookId", book.getId()));
        try {
            removeGenresRelationsFor(book);
            batchInsertGenresRelationsFor(book);
        } catch (RuntimeException e) {
            throw new EntityNotFoundException(String.format("Book not found (id = %s)", book.getId()));
        }
        return book;
    }

    private void batchInsertGenresRelationsFor(Book book) {
        List<Object[]> batchArgs = new ArrayList<>();
        book.getGenres().forEach(genre -> batchArgs.add(new Object[]{book.getId(), genre.getId()}));
        jdbc.batchUpdate("INSERT INTO books_genres (book_id, genre_id) VALUES (?, ?)", batchArgs);
    }

    private void removeGenresRelationsFor(Book book) {
        namedParameterJdbcOperations.update("DELETE FROM books_genres WHERE book_id = :id",
                Collections.singletonMap("id",book.getId()));
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            String title = rs.getString("title");
            Author author = new Author(rs.getLong("author_id"), rs.getString("full_name"));
            return new Book(id, title, author, Collections.emptyList());
        }
    }

    @SuppressWarnings("ClassCanBeRecord")
    @RequiredArgsConstructor
    private static class BookResultSetExtractor implements ResultSetExtractor<Book> {

        @Override
        public Book extractData(ResultSet rs) throws SQLException, DataAccessException {
            Book book = null;
            Author author = null;
            List<Genre> genres = new ArrayList<>();
            while (rs.next()) {
                if (author == null) {
                    author = newAuthor(rs);
                }
                genres.add(newGenre(rs));
                if (book == null) {
                    book = newBook(rs, author, genres);
                }
            }
            return book;
        }

        private Author newAuthor(ResultSet rs) throws SQLException {
            long authorId = rs.getLong("author_id");
            String authorName = rs.getString("full_name");
            return new Author(authorId, authorName);
        }

        private Book newBook(ResultSet rs, Author author, List<Genre> genres) throws SQLException {
            long bookId = rs.getLong("id");
            String title = rs.getString("title");
            return new Book(bookId, title, author, genres);
        }

        private Genre newGenre(ResultSet rs) throws SQLException {
            long genreId = rs.getLong("genre_id");
            String genreName = rs.getString("name");
            return new  Genre(genreId, genreName);
        }
    }


    private record BookGenreRelation(long bookId, long genreId) {
    }
}

