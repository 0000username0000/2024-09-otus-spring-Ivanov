package ru.otus.auth.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.auth.exceptions.EntityNotFoundException;
import ru.otus.auth.models.User;
import ru.otus.auth.repository.UserRepository;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getByLogin(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User not found: %s", login)));
    }
}