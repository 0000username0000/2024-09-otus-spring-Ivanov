package ru.otus.hw.config.batch.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.BookDocument;

@Component
public class BookProcessor implements ItemProcessor<Book, BookDocument> {

    @Override
    public BookDocument process(Book book) {
        return new BookDocument(null, book.getTitle(), book.getAuthor(), book.getGenres());
    }
}

