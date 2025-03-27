package ru.otus.hw.models.entities;

import lombok.Data;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.util.UUID;

@Data
@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

    @Serial
    private static final long serialVersionUID = 1410998928414592994L;

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Override
    public String getAuthority() {
        return name;
    }
}
