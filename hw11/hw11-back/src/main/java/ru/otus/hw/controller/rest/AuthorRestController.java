package ru.otus.hw.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.services.AuthorServiceImpl;
import ru.otus.hw.services.dto.AuthorDtoService;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorRestController {

    private final AuthorServiceImpl authorServiceImpl;

    private final AuthorDtoService authorDtoService;

    @GetMapping
    public Flux<AuthorDto> getAllAuthors() {
        Flux<Author> authors = authorServiceImpl.findAll();
        return authorDtoService.toDtoList(authors);
    }
}