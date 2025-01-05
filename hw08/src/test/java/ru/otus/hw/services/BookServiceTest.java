package ru.otus.hw.services;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(BookService.class)
@AutoConfigureDataMongo
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookServiceTest {

    @Autowired
    private BookService bookService;
    @Autowired
    private MongoTemplate mongoTemplate;

    private static final String NEW_TITLE_BOOK = "New Title";

    private Book book1;
    private Book book2;

    @BeforeEach
    void setUp() {
        mongoTemplate.getDb().drop();

        Author author = new Author(null, "Author 1");
        Genre genre = new Genre(null, "Genre 1");

        author = mongoTemplate.save(author);
        genre = mongoTemplate.save(genre);

        book1 = new Book(null, "Book 1", author, List.of(genre));
        book2 = new Book(null, "Book 2", author, List.of(genre));

        bookService.save(book1);
        bookService.save(book2);
    }

    @Test
    void shouldFindExpectedBookById() {
        val optionalBook = bookService.findById(book1.getId());
        assertThat(optionalBook).isPresent().get().usingRecursiveComparison().isEqualTo(book1);
    }

    @Test
    void shouldFindAllBooks() {
        val books = bookService.findAll();
        assertThat(books).isNotNull().hasSize(2)
                .allMatch(s -> !s.getTitle().isEmpty())
                .allMatch(s -> s.getGenres() != null && !s.getGenres().isEmpty())
                .allMatch(s -> s.getAuthor().getFullName() != null);
    }

    @Test
    void shouldUpdateBook() {
        val book = mongoTemplate.findById(book1.getId(), Book.class);
        String title = book.getTitle();
        book.setTitle("New Title");
        bookService.save(book);
        val updateBook = mongoTemplate.findById(book1.getId(), Book.class);
        assertThat(updateBook.getTitle()).isNotEqualTo(title).isEqualTo("New Title");
    }

    @Test
    void shouldSaveNewBook() {
        val newBook = new Book();
        newBook.setTitle("New Title");
        bookService.save(newBook);
        assertThat(newBook.getId()).isNotNull();
    }

    @Test
    void shouldDeleteById() {
        val book = mongoTemplate.findById(book1.getId(), Book.class);
        assertThat(book).isNotNull();
        bookService.deleteById(book1.getId());
        val deletedBook = mongoTemplate.findById(book1.getId(), Book.class);
        assertThat(deletedBook).isNull();
    }
}
