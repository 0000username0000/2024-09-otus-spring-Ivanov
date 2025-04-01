package ru.otus.auth.services;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.otus.auth.exceptions.EntityNotFoundException;
import ru.otus.auth.models.User;
import ru.otus.auth.repository.UserRepository;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public User getByLoginNN(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User not found: %s", login)));
    }


    public boolean validatePassword(String rawPassword, String encodedPassword) {
        System.out.println(rawPassword);
        System.out.println(encodedPassword);
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}