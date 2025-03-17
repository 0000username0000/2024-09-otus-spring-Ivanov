package ru.otus.hw.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name = "user")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = -5621805109925613372L;

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    // Роли: ADMIN, MANAGER, CLIENT
    @Column(nullable = false)
    private String role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Order> orders;
}
