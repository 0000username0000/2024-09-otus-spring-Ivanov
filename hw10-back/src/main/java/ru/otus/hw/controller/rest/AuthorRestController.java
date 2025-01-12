package ru.otus.hw.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.dto.AuthorDtoService;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorRestController {

    private final AuthorService authorService;
    private final AuthorDtoService authorDtoService;

    @GetMapping
    public List<AuthorDto> getAllAuthors() {
        List<Author> authors = authorService.findAll();
        return authorDtoService.toDtoList(authors);
    }
}