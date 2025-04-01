package ru.otus.auth.services;

import ru.otus.auth.models.User;

import java.util.List;

public interface UserService {

    User getByLoginNN(String login);

    boolean validatePassword(String rawPassword, String encodedPassword);

    boolean isAdmin(User user);

    List<User> findAll();
}