package ru.otus.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.auth.models.Users;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByUsername(String username);
}
