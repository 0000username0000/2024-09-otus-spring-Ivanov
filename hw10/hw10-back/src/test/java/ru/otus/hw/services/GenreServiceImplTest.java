package ru.otus.hw.services;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Genre;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(GenreServiceImpl.class)
public class GenreServiceImplTest {

    @Autowired
    private GenreServiceImpl genreServiceImpl;

    @Autowired
    private TestEntityManager testEntityManager;

    private Genre genre1;
    private Genre genre2;
    private Genre genre3;

    @BeforeEach
    void setUp() {
        genre1 = new Genre();
        genre1.setName("Genre 1");
        genre2 = new Genre();
        genre2.setName("Genre 2");
        genre3 = new Genre();
        genre3.setName("Genre 3");

        testEntityManager.persist(genre1);
        testEntityManager.persist(genre2);
        testEntityManager.persist(genre3);
    }

    @Test
    void shouldFindAllByIds() {
        Set<Long> ids = new HashSet<>();
        ids.add(genre1.getId());
        ids.add(genre2.getId());

        List<Genre> genres = genreServiceImpl.findAllByIds(ids);

        assertThat(genres).hasSize(2);
        assertThat(genres).extracting(Genre::getName).containsExactlyInAnyOrder("Genre 1", "Genre 2");
    }
}
