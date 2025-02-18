package ru.otus.hw.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.mapper.dto.AuthorDtoMapper;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorRestController {

    private final AuthorService authorService;

    private final AuthorDtoMapper authorDtoMapper;

    @GetMapping
    public List<AuthorDto> getAllAuthors() {
        List<Author> authors = authorService.findAll();
        return authorDtoMapper.toDtoList(authors);
    }
}