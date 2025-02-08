package ru.otus.hw.component;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Users;
import ru.otus.hw.repositories.UsersRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsersRepository usersRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        Users users = new Users(null, "user", passwordEncoder.encode("password"), "USER");
        Users admin = new Users(null, "admin", passwordEncoder.encode("password"), "ADMIN");
        usersRepository.saveAll(List.of(users, admin));
        System.out.println("Созданы пользователи: user/password, admin/password, passwordCrypt: " + passwordEncoder.encode("password"));
    }
}
