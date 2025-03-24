package ru.otus.hw.services;

import ru.otus.hw.models.entities.User_;

import java.util.UUID;

public interface UserService {

    public User_ findByIdNN(UUID uuid);
}
