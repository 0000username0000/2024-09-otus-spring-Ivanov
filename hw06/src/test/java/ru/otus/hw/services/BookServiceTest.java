package ru.otus.hw.services;

import lombok.val;
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
    private static final long FIRST_STUDENT_ID = 1L;

    @Test
    void shouldFindExpectedBookById() {
       val optionalBook = bookService.findById(FIRST_STUDENT_ID);
       val expectedBook = testEntityManager.find(Book.class, FIRST_STUDENT_ID);
       assertThat(optionalBook).isPresent().get().usingRecursiveComparison().isEqualTo(expectedBook);
    }
}
