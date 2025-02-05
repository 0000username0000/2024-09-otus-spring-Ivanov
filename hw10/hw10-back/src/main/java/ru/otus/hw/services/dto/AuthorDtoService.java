package ru.otus.hw.services.dto;

import org.springframework.stereotype.Service;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorDtoService {

    public AuthorDto toDto(Author author) {
        return new AuthorDto(author.getId(), author.getFullName());
    }

    public Author toEntity(AuthorDto authorDto) {
        return new Author(authorDto.getId(), authorDto.getFullName());
    }

    public List<AuthorDto> toDtoList(List<Author> authors) {
        return authors.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}