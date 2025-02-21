package ru.otus.hw.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DataInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        jdbcTemplate.execute("INSERT INTO author(full_name) VALUES ('author1'), ('author2'), ('author3'), ('author4'), ('author5')");
        jdbcTemplate.execute("INSERT INTO genre(name) VALUES ('genre1'), ('genre2'), ('genre3'), ('genre4'), ('genre5'), ('genre6'), ('genre7'), ('genre8'), ('genre9'), ('genre10'), ('genre11'), ('genre12'), ('genre13'), ('genre14'), ('genre15'), ('genre16')");
        jdbcTemplate.execute("INSERT INTO book(title, author_id) VALUES ('book1', 1), ('book2', 2), ('book3', 2), ('book4', 2), ('book5', 3), ('book6', 4), ('book7', 3), ('book8', 5)");
        jdbcTemplate.execute("INSERT INTO comment(text, book_id) VALUES ('first comment', 2), ('second comment', 2), ('third comment', 2)");
        jdbcTemplate.execute("INSERT INTO users(username, password, roles) VALUES('admin', '$2a$10$ir4MmvR8Gk4IJGMokZtdOOpXwcCh6T67SINeoPRW.K8rwdzroR9Pi', 'ADMIN')");
        jdbcTemplate.execute("INSERT INTO users(username, password, roles) VALUES('user', '$2a$10$ir4MmvR8Gk4IJGMokZtdOOpXwcCh6T67SINeoPRW.K8rwdzroR9Pi', 'USER')");

        jdbcTemplate.execute("INSERT INTO acl_class (id, class) VALUES (1, 'ru.otus.hw.models.Book');");
        jdbcTemplate.execute("INSERT INTO acl_class (id, class) VALUES (2, 'ru.otus.hw.models.Author');");
        jdbcTemplate.execute("INSERT INTO acl_class (id, class) VALUES (3, 'ru.otus.hw.models.Genre');");

        jdbcTemplate.execute("INSERT INTO acl_sid (principal, sid) VALUES (1, 'ROLE_ADMIN'), (1, 'ROLE_USER'), (0, 'ROLE_EDITOR');");
        jdbcTemplate.execute("INSERT INTO acl_object_identity (object_id_class, object_id_identity, \n" +
                "  parent_object, owner_sid, entries_inheriting) VALUES\n" +
                "  (1, 1, NULL, 1, 0),\n" +
                "  (1, 2, NULL, 1, 0),\n" +
                "  (1, 3, NULL, 1, 0);");
        jdbcTemplate.execute("INSERT INTO acl_entry \n" +
                "  (acl_object_identity, ace_order, \n" +
                "  sid, mask, granting, audit_success, audit_failure) \n" +
                "  VALUES\n" +
                "  (1, 1, 1, 1, 1, 1, 1),\n" +
                "  (1, 2, 1, 2, 1, 1, 1),\n" +
                "  (1, 3, 3, 1, 1, 1, 1),\n" +
                "  (2, 1, 2, 1, 1, 1, 1),\n" +
                "  (2, 2, 3, 1, 1, 1, 1),\n" +
                "  (3, 1, 3, 1, 1, 1, 1),\n" +
                "  (3, 2, 3, 2, 1, 1, 1);");
    }
}
