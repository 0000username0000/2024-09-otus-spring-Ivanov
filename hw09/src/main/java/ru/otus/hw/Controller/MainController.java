package ru.otus.hw.Controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class MainController {

    //http://localhost:8080/
    @GetMapping("/")
    public String mainPage() {
        return "main";
    }
}
