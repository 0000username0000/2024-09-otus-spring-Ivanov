package ru.otus.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.auth.models.Role;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByName(String name);

    Set<Role> findAllByNameIn(List<String> names);
}
