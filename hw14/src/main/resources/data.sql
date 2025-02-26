insert into author(full_name)
values ('author1'), ('author2'), ('author3'), ('author4'), ('author5');

insert into genre(name)
values ('genre1'), ('genre2'), ('genre3'), ('genre4'), ('genre5'), ('genre6'), ('genre7'), ('genre8'),
       ('genre9'), ('genre10'), ('genre11'), ('genre12'), ('genre13'), ('genre14'), ('genre15'), ('genre16');

insert into book(title, author_id)
values ('book1', 1), ('book2', 2), ('book3', 2), ('book4', 2),
       ('book5', 3), ('book6', 4), ('book7', 3), ('book8', 5);

insert into books_genres(genre_id, book_id)
values (1, 1), (1, 2), (1, 3), (2, 1), (3, 1), (4, 5), (4, 6), (4, 7), (5, 7), (6, 8), (7, 8), (8, 8);

insert into comment(text, book_id)
values ('first comment', 2), ('second comment', 2), ('third comment', 2);

INSERT INTO roles (name) VALUES ('ADMIN'), ('USER'), ('EDITOR');

INSERT INTO users (username, password) VALUES
('admin', '$2a$10$ir4MmvR8Gk4IJGMokZtdOOpXwcCh6T67SINeoPRW.K8rwdzroR9Pi'),
('user', '$2a$10$ir4MmvR8Gk4IJGMokZtdOOpXwcCh6T67SINeoPRW.K8rwdzroR9Pi');

INSERT INTO users_roles (user_id, role_id)
VALUES (1, 1), (1, 2), (2, 2);

INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
SELECT id, 1, (SELECT id FROM acl_sid WHERE sid = 'admin'), 1, 1, 1, 1
FROM acl_object_identity;

INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
SELECT id, 2, (SELECT id FROM acl_sid WHERE sid = 'admin'), 2, 1, 1, 1
FROM acl_object_identity;

INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
SELECT id, 3, (SELECT id FROM acl_sid WHERE sid = 'user'), 1, 1, 1, 1
FROM acl_object_identity
WHERE object_id_identity IN (1, 2, 3);

