package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.entities.User;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
