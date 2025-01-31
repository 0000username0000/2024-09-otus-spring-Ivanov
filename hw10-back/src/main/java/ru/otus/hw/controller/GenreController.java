package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.GenreServiceImpl;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class GenreController {

    private final GenreServiceImpl genreServiceImpl;

    @GetMapping("/genres")
    public String getListPage(Model model) {
        List<Genre> genres = genreServiceImpl.findAll();
        model.addAttribute("genres", genres);
        return "genres";
    }
}
