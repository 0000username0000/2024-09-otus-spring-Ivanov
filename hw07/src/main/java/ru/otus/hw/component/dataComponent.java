package ru.otus.hw.component;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Author;
import ru.otus.hw.services.AuthorService;

@Component
@AllArgsConstructor
public class dataComponent {

    private final AuthorService authorService;

    public void addAuthor() {
        Author author1 = new Author();
        author1.setFullName("author1");
    }
}
