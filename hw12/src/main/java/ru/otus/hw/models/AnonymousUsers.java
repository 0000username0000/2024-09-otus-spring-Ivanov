package ru.otus.hw.models;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class AnonymousUsers extends Users {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return "anonymousUser";
    }
}
