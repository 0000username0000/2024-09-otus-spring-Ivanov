package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.models.Genre;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import(GenreService.class)
@AutoConfigureDataMongo
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GenreServiceTest {

    @Autowired
    private GenreService genreService;

    @Autowired
    private MongoTemplate mongoTemplate;

    private Genre genre1;
    private Genre genre2;
    private Genre genre3;

    @BeforeEach
    void setUp() {
        // Очистка базы данных перед каждым тестом
        mongoTemplate.getDb().drop();

        // Подготовка тестовых данных
        genre1 = new Genre(null, "Genre 1");
        genre2 = new Genre(null, "Genre 2");
        genre3 = new Genre(null, "Genre 3");

        genre1 = mongoTemplate.save(genre1);
        genre2 = mongoTemplate.save(genre2);
        genre3 = mongoTemplate.save(genre3);
    }

    @Test
    void shouldFindAllByIds() {
        Set<String> ids = new HashSet<>();
        ids.add(genre1.getId());
        ids.add(genre2.getId());

        List<Genre> genres = genreService.findAllByIds(ids);

        assertThat(genres).hasSize(2);
        assertThat(genres).extracting(Genre::getName).containsExactlyInAnyOrder("Genre 1", "Genre 2");
    }
}
