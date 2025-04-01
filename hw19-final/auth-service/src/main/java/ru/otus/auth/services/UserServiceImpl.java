package ru.otus.auth.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.otus.auth.exceptions.EntityNotFoundException;
import ru.otus.auth.models.Role;
import ru.otus.auth.models.User;
import ru.otus.auth.repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User getByLoginNN(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User not found: %s", login)));
    }

    @Override
    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public boolean isAdmin(User user) {
        return user.getRoles().stream().map(Role::getName).anyMatch("ADMIN"::equals);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
}