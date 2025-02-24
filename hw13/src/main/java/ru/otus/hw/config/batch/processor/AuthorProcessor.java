package ru.otus.hw.config.batch.processor;

import org.springframework.batch.item.ItemProcessor;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.mongo.AuthorDocument;
import org.springframework.stereotype.Component;

@Component
public class AuthorProcessor implements ItemProcessor<Author, AuthorDocument> {
    @Override
    public AuthorDocument process(Author author) {
        return new AuthorDocument(null, author.getFullName());
    }
}