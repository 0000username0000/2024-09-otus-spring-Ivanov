package ru.otus.hw.mapper.dto;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Genre;

@Component
public class GenreDtoMapper {

    public GenreDto toDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }
}
