package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
//import org.springframework.shell.standard.ShellComponent;
//import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.GenreService;

@RequiredArgsConstructor
//@ShellComponent
public class DataCommands {

    private final AuthorService authorService;
    
    private final GenreService genreService;

    public void addAuthor() {
        for (int i = 0; i <= 5; i++) {
            Author author = new Author();
            author.setFullName(String.format("author%s", i));
            authorService.save(author);
        }
    }

    public void addGenre() {
        for (int i = 0; i <= 7; i++) {
            Genre genre = new Genre();
            genre.setName(String.format("genre%s", i));
            genreService.save(genre);
        }
    }

//    @ShellMethod(value = "Init data", key = "data")
    public void initData() {
        addAuthor();
        addGenre();
    }
}
