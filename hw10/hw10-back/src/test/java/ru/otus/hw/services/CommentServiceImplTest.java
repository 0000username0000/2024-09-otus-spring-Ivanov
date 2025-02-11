package ru.otus.hw.services;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(CommentServiceImpl.class)
public class CommentServiceImplTest {

    private static final long FIRST_COMMENT_ID = 1L;
    private static final long BOOK_ID = 2L;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        testEntityManager.clear();
    }

    @Test
    void shouldFindExpectedCommentById() {
        val optionalComment = commentService.findByIdNN(FIRST_COMMENT_ID);
        val expectedComment = testEntityManager.find(Comment.class, FIRST_COMMENT_ID);
        assertThat(optionalComment).usingRecursiveComparison().isEqualTo(expectedComment);
    }

    @Test
    void shouldSaveNewComment() {
        val newComment = new Comment();
        newComment.setText("New Comment");
        Book book = testEntityManager.find(Book.class, BOOK_ID);
        newComment.setBook(book);
        commentService.save(newComment);
        assertThat(newComment.getId()).isGreaterThan(0);
        val savedComment = testEntityManager.find(Comment.class, newComment.getId());
        assertThat(savedComment).isNotNull();
        assertThat(savedComment.getText()).isEqualTo("New Comment");
    }

    @Test
    void shouldDeleteById() {
        val comment = testEntityManager.find(Comment.class, FIRST_COMMENT_ID);
        assertThat(comment).isNotNull();
        commentService.deleteById(FIRST_COMMENT_ID);
        val deletedComment = testEntityManager.find(Comment.class, FIRST_COMMENT_ID);
        assertThat(deletedComment).isNull();
    }

    @Test
    void shouldFindCommentsByBookId() {
        val comments = commentService.findByBookId(BOOK_ID);
        assertThat(comments.size()).isEqualTo(3);
    }
}
