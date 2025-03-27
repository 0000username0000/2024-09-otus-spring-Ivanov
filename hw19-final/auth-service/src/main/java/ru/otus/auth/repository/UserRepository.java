package ru.otus.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.auth.models.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByLogin(String login);
}
