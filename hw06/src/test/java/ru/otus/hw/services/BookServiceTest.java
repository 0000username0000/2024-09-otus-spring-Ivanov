package ru.otus.hw.services;

import lombok.val;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Book;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(BookService.class)
public class BookServiceTest {

    @Autowired
    private BookService bookService;
    @Autowired
    private TestEntityManager testEntityManager;

    private static final long FIRST_BOOK_ID = 1L;
    private static final int EXPECTED_NUMBER_OF_BOOKS = 8;
    private static final int EXPECTED_QUERIES_COUNT = 9;
    private static final String NEW_TITLE_BOOK = "New Title";

    @Test
    void shouldFindExpectedBookById() {
       val optionalBook = bookService.findById(FIRST_BOOK_ID);
       val expectedBook = testEntityManager.find(Book.class, FIRST_BOOK_ID);
       assertThat(optionalBook).isPresent().get().usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @Test
    void shouldFindAllBooks() {
        SessionFactory sessionFactory = testEntityManager.getEntityManager()
                .getEntityManagerFactory().unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);

        System.out.println("\n\n\n\n----------------------------------------------------------------------------------------------------------");
        val books = bookService.findAll();
        assertThat(books).isNotNull().hasSize(EXPECTED_NUMBER_OF_BOOKS)
                .allMatch(s -> !s.getTitle().isEmpty())
                .allMatch(s -> s.getGenres() != null && !s.getGenres().isEmpty())
                .allMatch(s -> s.getAuthor().getFullName() != null);
        System.out.println("----------------------------------------------------------------------------------------------------------\n\n\n\n");
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(EXPECTED_QUERIES_COUNT);
    }

    @Test
    void shouldUpdateBook() {
        val book = testEntityManager.find(Book.class, FIRST_BOOK_ID);
        String title = book.getTitle();
        book.setTitle(NEW_TITLE_BOOK);
        bookService.save(book);
        val updateBook = testEntityManager.find(Book.class, FIRST_BOOK_ID);
        assertThat(updateBook.getTitle()).isNotEqualTo(title).isEqualTo(NEW_TITLE_BOOK);
    }

    @Test
    void shouldSaveNewBook() {
        val newBook = new Book();
        newBook.setTitle("New Title");
        newBook.setId(0);
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
