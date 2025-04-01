package ru.otus.hw.services;

import ru.otus.hw.models.entities.User;

import java.util.UUID;

public interface UserService {

    public User findByIdNN(UUID uuid);
}
