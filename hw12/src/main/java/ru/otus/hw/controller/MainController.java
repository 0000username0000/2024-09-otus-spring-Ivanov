package ru.otus.hw.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class MainController {

    @GetMapping("/")
    public String indexPage() {
        return "index";
    }

    @GetMapping("/main")
    public String mainPage() {
        return "main";
    }

    @GetMapping("/public")
    public String publicPage() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        System.out.printf("user: %s%n", authentication.getPrincipal());
        return "public";
    }


    @GetMapping("/error")
    public String errorPage(Model model) {
        model.addAttribute("source", "errorPage");
        return "error";
    }

    @PostMapping("/fail")
    public String failPage(Model model) {
        model.addAttribute("source", "wrong password or username");
        return "error";
    }
}
