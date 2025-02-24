package ru.otus.hw.services;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Book;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(BookServiceImpl.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookServiceImplTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private TestEntityManager testEntityManager;

    private static final long FIRST_BOOK_ID = 1L;
    private static final int EXPECTED_NUMBER_OF_BOOKS = 8;
    private static final String NEW_TITLE_BOOK = "New Title";

    @Test
    void shouldFindExpectedBookById() {
        val optionalBook = bookService.findByIdNN(FIRST_BOOK_ID);
        val expectedBook = testEntityManager.find(Book.class, FIRST_BOOK_ID);
        assertThat(optionalBook).usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @Test
    void shouldFindAllBooks() {
        val books = bookService.findAll();
        assertThat(books).isNotNull().hasSize(EXPECTED_NUMBER_OF_BOOKS);
    }

    @Test
    void shouldUpdateBook() {
        val book = testEntityManager.find(Book.class, FIRST_BOOK_ID);
        String title = book.getTitle();
        book.setTitle(NEW_TITLE_BOOK);
        bookService.save(book);
        val updatedBook = testEntityManager.find(Book.class, FIRST_BOOK_ID);
        assertThat(updatedBook.getTitle()).isNotEqualTo(title).isEqualTo(NEW_TITLE_BOOK);
    }

    @Test
    void shouldSaveNewBook() {
        val newBook = new Book();
        newBook.setTitle("New Title");
        bookService.save(newBook);
        assertThat(newBook.getId()).isGreaterThan(0);
    }

    @Test
    void shouldDeleteById() {
        val book = testEntityManager.find(Book.class, FIRST_BOOK_ID);
        assertThat(book).isNotNull();
        bookService.deleteById(FIRST_BOOK_ID);
        val deletedBook = testEntityManager.find(Book.class, FIRST_BOOK_ID);
        assertThat(deletedBook).isNull();
    }
}
