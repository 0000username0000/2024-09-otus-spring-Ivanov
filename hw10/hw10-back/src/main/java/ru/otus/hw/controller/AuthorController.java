package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw.models.Author;
import ru.otus.hw.services.AuthorServiceImpl;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorServiceImpl authorServiceImpl;

    @GetMapping("/authors")
    public String getListPage(Model model) {
        List<Author> authors = authorServiceImpl.findAll();
        model.addAttribute("authors", authors);
        return "authors";
    }
}
