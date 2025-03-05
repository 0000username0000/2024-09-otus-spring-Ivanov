package ru.otus.hw.services;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasPermission(#id, 'Author', 'READ')")
    public Optional<Author> findById(long id) {
        return authorRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasRole('ROLE_ADMIN') or hasPermission(filterObject, 'READ')")
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasPermission(#author, 'WRITE')")
    public Author save(Author author) {
        return authorRepository.save(author);
    }
}
