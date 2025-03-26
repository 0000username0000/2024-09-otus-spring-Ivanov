package ru.otus.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.auth.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLogin(String login);
}
