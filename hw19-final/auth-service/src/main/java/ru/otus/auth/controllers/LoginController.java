package ru.otus.auth.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.auth.dto.JwtRequest;
import ru.otus.auth.dto.JwtResponse;
import ru.otus.auth.services.AuthService;

import java.time.Duration;

@Controller
@RequestMapping("/form-auth")
@RequiredArgsConstructor
public class LoginController {

    private final AuthService authService;

    @GetMapping("/login")
    public String showLoginPage(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Неверный логин или пароль");
        }
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String login,
                               @RequestParam String password,
                               HttpServletResponse response) {
        try {
            JwtResponse jwtResponse = authService.login(new JwtRequest(login, password));

            ResponseCookie cookie = ResponseCookie.from("token", jwtResponse.getAccessToken())
                    .httpOnly(true)
                    .secure(false) // true в production
                    .path("/")
                    .maxAge(Duration.ofHours(1))
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            return "redirect:/";
        } catch (Exception e) {
            return "redirect:/form-auth/login?error=true";
        }
    }
}
