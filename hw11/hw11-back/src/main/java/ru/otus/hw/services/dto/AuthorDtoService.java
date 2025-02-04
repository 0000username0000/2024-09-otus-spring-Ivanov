package ru.otus.hw.services.dto;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.models.Author;

@Service
public class AuthorDtoService {

    public Mono<AuthorDto> toDto(Mono<Author> authorMono) {
        return authorMono.map(author -> new AuthorDto(author.getId(), author.getFullName()));
    }

    public Mono<Author> toEntity(Mono<AuthorDto> authorDtoMono) {
        return authorDtoMono.map(authorDto -> new Author(authorDto.getId(), authorDto.getFullName()));
    }

    public Flux<AuthorDto> toDtoList(Flux<Author> authorsFlux) {
        return authorsFlux.map(this::toDto);
    }

    private AuthorDto toDto(Author author) {
        return new AuthorDto(author.getId(), author.getFullName());
    }

    private Author toEntity(AuthorDto authorDto) {
        return new Author(authorDto.getId(), authorDto.getFullName());
    }
}
