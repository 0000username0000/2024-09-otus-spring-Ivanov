package ru.otus.hw.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.entities.User_;
import ru.otus.hw.repositories.UserRepository;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User_ findByIdNN(UUID uuid) {
        return userRepository.findById(uuid).orElseThrow(() ->
                new EntityNotFoundException(String.format("User not found with id = %s", uuid)));
    }
}
