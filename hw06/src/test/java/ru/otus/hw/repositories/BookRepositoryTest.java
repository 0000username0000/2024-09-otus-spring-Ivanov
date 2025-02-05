package ru.otus.hw.repositories;

import lombok.val;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Book;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
<<<<<<<< HEAD:hw10/hw10-back/src/test/java/ru/otus/hw/services/BookServiceImplTest.java
@Import(BookServiceImpl.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookServiceImplTest {

    @Autowired
    private BookServiceImpl bookServiceImpl;
========
@Import(JpaBookRepository.class)
public class BookRepositoryTest {

    @Autowired
    private JpaBookRepository bookRepository;
>>>>>>>> 4fa3896e326c68a10592b47e5922472b6d37bd3f:hw06/src/test/java/ru/otus/hw/repositories/BookRepositoryTest.java
    @Autowired
    private TestEntityManager testEntityManager;

    private static final long FIRST_BOOK_ID = 1L;
    private static final int EXPECTED_NUMBER_OF_BOOKS = 8;
    private static final int EXPECTED_QUERIES_COUNT = 14;
    private static final String NEW_TITLE_BOOK = "New Title";

    @Test
    void shouldFindExpectedBookById() {
<<<<<<<< HEAD:hw10/hw10-back/src/test/java/ru/otus/hw/services/BookServiceImplTest.java
       val optionalBook = bookServiceImpl.findByIdNN(FIRST_BOOK_ID);
========
       val optionalBook = bookRepository.findById(FIRST_BOOK_ID);
>>>>>>>> 4fa3896e326c68a10592b47e5922472b6d37bd3f:hw06/src/test/java/ru/otus/hw/repositories/BookRepositoryTest.java
       val expectedBook = testEntityManager.find(Book.class, FIRST_BOOK_ID);
       assertThat(optionalBook).usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @Test
    void shouldFindAllBooks() {
        SessionFactory sessionFactory = testEntityManager.getEntityManager()
                .getEntityManagerFactory().unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);

        System.out.println("\n\n\n\n----------------------------------------------------------------------------------------------------------");
<<<<<<<< HEAD:hw10/hw10-back/src/test/java/ru/otus/hw/services/BookServiceImplTest.java
        val books = bookServiceImpl.findAll();
========
        val books = bookRepository.findAll();
>>>>>>>> 4fa3896e326c68a10592b47e5922472b6d37bd3f:hw06/src/test/java/ru/otus/hw/repositories/BookRepositoryTest.java
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
<<<<<<<< HEAD:hw10/hw10-back/src/test/java/ru/otus/hw/services/BookServiceImplTest.java
        bookServiceImpl.save(book);
========
        bookRepository.save(book);
>>>>>>>> 4fa3896e326c68a10592b47e5922472b6d37bd3f:hw06/src/test/java/ru/otus/hw/repositories/BookRepositoryTest.java
        val updateBook = testEntityManager.find(Book.class, FIRST_BOOK_ID);
        assertThat(updateBook.getTitle()).isNotEqualTo(title).isEqualTo(NEW_TITLE_BOOK);
    }

    @Test
    void shouldSaveNewBook() {
        val newBook = new Book();
        newBook.setTitle("New Title");
        newBook.setId(0);
<<<<<<<< HEAD:hw10/hw10-back/src/test/java/ru/otus/hw/services/BookServiceImplTest.java
        bookServiceImpl.save(newBook);
========
        bookRepository.save(newBook);
>>>>>>>> 4fa3896e326c68a10592b47e5922472b6d37bd3f:hw06/src/test/java/ru/otus/hw/repositories/BookRepositoryTest.java
        assertThat(newBook.getId()).isGreaterThan(0);
    }

    @Test
    void shouldDeleteById() {
        val book = testEntityManager.find(Book.class, FIRST_BOOK_ID);
        assertThat(book).isNotNull();
<<<<<<<< HEAD:hw10/hw10-back/src/test/java/ru/otus/hw/services/BookServiceImplTest.java
        bookServiceImpl.deleteById(FIRST_BOOK_ID);
========
        bookRepository.deleteById(FIRST_BOOK_ID);
>>>>>>>> 4fa3896e326c68a10592b47e5922472b6d37bd3f:hw06/src/test/java/ru/otus/hw/repositories/BookRepositoryTest.java
        val deletedBook = testEntityManager.find(Book.class, FIRST_BOOK_ID);
        assertThat(deletedBook).isNull();
    }
}
