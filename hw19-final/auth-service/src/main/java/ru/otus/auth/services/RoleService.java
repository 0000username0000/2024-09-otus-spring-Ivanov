package ru.otus.auth.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.auth.models.Role;
import ru.otus.auth.repository.RoleRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role findByNameNN(String name) {
        return roleRepository.findByName(name).orElseThrow(() -> new RuntimeException("Role not found"));
    }

    public Set<Role> findAllByNameIn(List<String> roleNames) {
        return new HashSet<>(roleRepository.findAllByNameIn(roleNames));
    }
}
