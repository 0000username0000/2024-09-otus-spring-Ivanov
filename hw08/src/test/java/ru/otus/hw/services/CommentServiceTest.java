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
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(CommentService.class)
@AutoConfigureDataMongo
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;
    @Autowired
    private MongoTemplate mongoTemplate;

    private Book book;

    @BeforeEach
    void setUp() {
        mongoTemplate.getDb().drop();

        book = new Book(null, "Book Title", null, List.of());
        book = mongoTemplate.save(book);

        Comment comment1 = new Comment(null, "Comment 1", book);
        Comment comment2 = new Comment(null, "Comment 2", book);
        Comment comment3 = new Comment(null, "Comment 3", book);

        mongoTemplate.save(comment1);
        mongoTemplate.save(comment2);
        mongoTemplate.save(comment3);
    }

    @Test
    void shouldFindExpectedCommentById() {
        val comment = mongoTemplate.findAll(Comment.class).get(0);
        val optionalComment = commentService.findById(comment.getId());
        assertThat(optionalComment).isPresent().get().usingRecursiveComparison().isEqualTo(comment);
    }

    @Test
    void shouldSaveNewComment() {
        val newComment = new Comment(null, "New Comment", book);
        commentService.save(newComment);
        assertThat(newComment.getId()).isNotNull();

        val savedComment = mongoTemplate.findById(newComment.getId(), Comment.class);
        assertThat(savedComment).isNotNull();
        assertThat(savedComment.getText()).isEqualTo("New Comment");
    }

    @Test
    void shouldDeleteById() {
        val comment = mongoTemplate.findAll(Comment.class).get(0);
        assertThat(comment).isNotNull();

        commentService.deleteById(comment.getId());
        val deletedComment = mongoTemplate.findById(comment.getId(), Comment.class);
        assertThat(deletedComment).isNull();
    }

    @Test
    void shouldFindCommentsByBookId() {
        val comments = commentService.findByBookId(book.getId());
        assertThat(comments).isNotNull().hasSize(3)
                .allMatch(c -> c.getBook().getId().equals(book.getId()))
                .allMatch(c -> c.getText() != null && !c.getText().isEmpty());
    }
}
