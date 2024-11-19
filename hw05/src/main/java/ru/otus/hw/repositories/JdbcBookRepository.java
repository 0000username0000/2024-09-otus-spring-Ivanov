package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final GenreRepository genreRepository;

    private final JdbcOperations jdbc;

    @Override
    public Optional<Book> findById(long id) {
        return Optional
                .ofNullable(jdbc.query("SELECT b.id, b.title, b.author_id, a.full_name, bg.genre_id, g.name " +
                                            "FROM books b " +
                                            "JOIN authors a ON a.id = b.author_id " +
                                            "JOIN books_genres bg ON bg.book_id = b.id " +
                                            "JOIN genres g ON g.id = bg.genre_id " +
                                            "WHERE b.id = ?",
                        new BookResultSetExtractor(), id));
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
        jdbc.update("DELETE FROM books WHERE id = ?", id);
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

//    private void mergeBooksInfo(List<Book> booksWithoutGenres, List<Genre> genres,
//                                List<BookGenreRelation> relations) {
//        Map<Long, Genre> genreMap = new HashMap<>();
//        genres.forEach(e -> genreMap.put(e.getId(), e));
//        Map<Long, List<Long>> bookGenreRelations = new HashMap<>();
//        relations.forEach(relation -> bookGenreRelations
//                .computeIfAbsent(relation.bookId(), k -> new ArrayList<>())
//                .add(relation.genreId()));
//        booksWithoutGenres.forEach(book -> {
//            List<Long> genreIds = bookGenreRelations.get(book.getId());
//            if (genreIds != null) {
//                List<Genre> genreList = new ArrayList<>();
//                genreIds.forEach(genreId -> {
//                    Genre genre = genreMap.get(genreId);
//                    if (genre != null) {
//                        genreList.add(genre);
//                    }
//                });
//                book.setGenres(genreList);
//            }
//        });
//    }

    private void mergeBooksInfo(List<Book> booksWithoutGenres, List<Genre> genres,
                                List<BookGenreRelation> relations) {
        Map<Long, Genre> genreMap = createGenreMap(genres);
        Map<Long, List<Long>> bookGenreRelations = createBookGenreRelationsMap(relations);
        booksWithoutGenres.forEach(book ->
                assignGenresToBook(book, bookGenreRelations.get(book.getId()), genreMap)
        );
    }

    private Map<Long, Genre> createGenreMap(List<Genre> genres) {
        Map<Long, Genre> genreMap = new HashMap<>();
        genres.forEach(genre -> genreMap.put(genre.getId(), genre));
        return genreMap;
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
                    .filter(genre -> genre != null)
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
        jdbc.update("UPDATE books SET title = ?, author_id = ? WHERE id = ?",
                book.getTitle(), book.getAuthor().getId(), book.getId());
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
        jdbc.update("DELETE FROM books_genres WHERE book_id = ?", book.getId());
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

    // Использовать для findById
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
                    long authorId = rs.getLong("author_id");
                    String authorName = rs.getString("full_name");
                    author = new Author(authorId, authorName);
                }
                long genreId = rs.getLong("genre_id");
                String genreName = rs.getString("name");
                Genre genre = new Genre(genreId, genreName);
                genres.add(genre);
                if (book == null) {
                    long bookId = rs.getLong("id");
                    String title = rs.getString("title");
                    book = new Book(bookId, title, author, genres);
                }
            }
            return book;
        }
    }


    private record BookGenreRelation(long bookId, long genreId) {
    }
}

