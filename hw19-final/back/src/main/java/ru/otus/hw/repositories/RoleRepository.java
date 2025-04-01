package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.entities.Role;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
}
