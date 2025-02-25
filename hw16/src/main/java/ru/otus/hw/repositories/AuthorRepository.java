package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.otus.hw.models.Author;

@RepositoryRestResource(path = "authors_rest")
public interface AuthorRepository extends JpaRepository<Author, Long> {

}
