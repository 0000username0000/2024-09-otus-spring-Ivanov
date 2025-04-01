package ru.otus.auth.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.auth.exceptions.EntityNotFoundException;
import ru.otus.auth.models.Role;
import ru.otus.auth.repository.RoleRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public Role findByNameNN(String name) {
        return roleRepository.findByName(name).orElseThrow(() ->
                new EntityNotFoundException(String.format("Role not found: %s", name)));
    }

    @Override
    @Transactional
    public Set<Role> findAllByNameIn(List<String> roleNames) {
        return new HashSet<>(roleRepository.findAllByNameIn(roleNames));
    }
}