package ru.otus.auth.services;

import ru.otus.auth.models.Role;

import java.util.List;
import java.util.Set;

public interface RoleService {

    Role findByNameNN(String name);

    Set<Role> findAllByNameIn(List<String> roleNames);
}

