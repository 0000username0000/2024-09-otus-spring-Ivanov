package ru.otus.hw.services;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DataMongoTest
@Import(BookServiceImp.class)
@AutoConfigureDataMongo
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookServiceImpTest {

    @Autowired
    private BookServiceImp bookService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @MockBean
    private GenreServiceImp genreServiceImp;

    @MockBean
    private AuthorServiceImp authorServiceImp;

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
        assertThat(optionalBook).usingRecursiveComparison().isEqualTo(book1);
        System.out.println(book1.toString());
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

    @Test
    void shouldThrowExceptionWhenAuthorNotFound() {
        Author nonexistentAuthor = new Author("nonexistent-id", "Nonexistent Author");
        Book bookWithoutAuthor = new Book();
        bookWithoutAuthor.setTitle("Book");
        bookWithoutAuthor.setAuthor(nonexistentAuthor);
        when(authorServiceImp.findByIdNN(nonexistentAuthor.getId())).thenThrow(new EntityNotFoundException("Author not found"));
        assertThatThrownBy(() -> bookService.save(bookWithoutAuthor)).isInstanceOf(EntityNotFoundException.class);
        verify(authorServiceImp).findByIdNN(nonexistentAuthor.getId());
    }

    @Test
    void shouldThrowExceptionWhenGenreNotFound() {
        Genre nonexistentGenre = new Genre("nonexistent-id", "Nonexistent Genre");
        Book bookWithoutGenre = new Book();
        bookWithoutGenre.setTitle("Book");
        bookWithoutGenre.setGenres(List.of(nonexistentGenre));
        when(genreServiceImp.findByIdNN(nonexistentGenre.getId())).thenThrow(new EntityNotFoundException("Genre not found"));
        assertThatThrownBy(() -> bookService.save(bookWithoutGenre)).isInstanceOf(EntityNotFoundException.class);
        verify(genreServiceImp).findByIdNN(nonexistentGenre.getId());
    }


    @Test
    void shouldThrowExceptionWhenBookNotFoundById() {
        String nonexistentId = "nonexistent-id";
        assertThatThrownBy(() -> bookService.findById(nonexistentId))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
