package ru.otus.hw.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class DatabaseShellCommands {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DatabaseShellCommands(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @ShellMethod(key = "ia")
    public void insertAuthors() {
        String sql = "INSERT INTO author (full_name) VALUES ('author1'), ('author2'), ('author3'), ('author4'), ('author5')";
        jdbcTemplate.update(sql);
        System.out.println("Authors inserted.");
    }

    @ShellMethod(key = "ig")
    public void insertGenres() {
        String sql = "INSERT INTO genre (name) VALUES ('genre1'), ('genre2'), ('genre3'), ('genre4'), ('genre5'), ('genre6'), ('genre7'), ('genre8'),"
                + "('genre9'), ('genre10'), ('genre11'), ('genre12'), ('genre13'), ('genre14'), ('genre15'), ('genre16')";
        jdbcTemplate.update(sql);
        System.out.println("Genres inserted.");
    }

    @ShellMethod(key = "ib")
    public void insertBooks() {
        String sql = "INSERT INTO book (title, author_id) VALUES ('book1', 1), ('book2', 2), ('book3', 2), ('book4', 2),"
                + "('genre5', 3), ('genre6', 4), ('genre7', 3), ('genre8', 5)";
        jdbcTemplate.update(sql);
        System.out.println("Books inserted.");
    }

    @ShellMethod(key = "ibg")
    public void insertBooksGenres() {
        String sql = "INSERT INTO books_genres (genre_id, book_id) VALUES (1, 1), (1, 2), (1, 3), (2, 1), (3, 1), (4, 5), (4, 6),"
                + "(4, 7), (5, 7), (6, 8), (7, 8), (8, 8)";
        jdbcTemplate.update(sql);
        System.out.println("Books genres inserted.");
    }

    @ShellMethod(key = "ic")
    public void insertComments() {
        String sql = "INSERT INTO comment (text, book_id) VALUES ('first comment', 2), ('second comment', 2), ('third comment', 2)";
        jdbcTemplate.update(sql);
        System.out.println("Comments inserted.");
    }
    @ShellMethod(key = "data")
    public void insertData() {
        insertAuthors();
        insertGenres();
        insertBooks();
        insertBooksGenres();
        insertComments();
    }

}
