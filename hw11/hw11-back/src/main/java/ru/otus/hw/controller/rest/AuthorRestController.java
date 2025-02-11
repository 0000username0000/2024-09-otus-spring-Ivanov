package ru.otus.hw.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.mapper.dto.AuthorDtoMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.services.AuthorService;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorRestController {

    private final AuthorService authorService;

    private final AuthorDtoMapper authorDtoMapper;

    @GetMapping
    public Flux<AuthorDto> getAllAuthors() {
        Flux<Author> authors = authorService.findAll();
        return authorDtoMapper.toDtoList(authors);
    }
}