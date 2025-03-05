package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;

@RepositoryRestResource(path = "genres_rest")
public interface GenreRepository extends JpaRepository<Genre, Long> {

    @RestResource(path = "genres", rel = "genres")
    List<Genre> findByIdIn(Set<Long> ids);

}
