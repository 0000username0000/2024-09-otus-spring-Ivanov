package ru.otus.hw.models.enums;

import lombok.Getter;

@Getter
public enum Role {

    ADMIN("Администратор"),
    MANAGER("Менеджер"),
    CLIENT("Клиент");

    private final String description;

    Role(String description) {
        this.description = description;
    }

    public boolean isAdmin() {
        return this == ADMIN;
    }

    public boolean isManager() {
        return this == MANAGER;
    }

    public boolean isClient() {
        return this == CLIENT;
    }

//    public GrantedAuthority toAuthority() {
//        return new SimpleGrantedAuthority("ROLE_" + this.name());
//    }
}
