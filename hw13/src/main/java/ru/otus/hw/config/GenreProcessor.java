package ru.otus.hw.config;

import org.springframework.batch.item.ItemProcessor;
import ru.otus.hw.models.Genre;
import ru.otus.hw.models.mongo.GenreDocument;

public class GenreProcessor implements ItemProcessor<Genre, GenreDocument> {

    @Override
    public GenreDocument process(Genre genre) throws Exception {
        return new GenreDocument(null, genre.getName());
    }
}
