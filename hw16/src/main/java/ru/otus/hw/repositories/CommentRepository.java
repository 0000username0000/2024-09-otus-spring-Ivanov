package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.otus.hw.models.Comment;

import java.util.List;

@RepositoryRestResource(path = "comments_rest")
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @RestResource(path = "book_comments", rel = "book_comments")
    List<Comment> findByBookId(Long bookId);

}
