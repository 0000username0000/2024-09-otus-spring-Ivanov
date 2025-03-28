package ru.otus.auth.components;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.auth.models.JwtAuthentication;


import java.util.List;
import java.util.Set;
import ru.otus.auth.models.Role;
import ru.otus.auth.services.RoleService;

@Component
@AllArgsConstructor
public final class JwtUtils {

    private final RoleService roleService;

    public JwtAuthentication generate(Claims claims) {
        final JwtAuthentication jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setRoles(getRoles(claims));
        jwtInfoToken.setFirstName(claims.get("firstName", String.class));
        jwtInfoToken.setUsername(claims.getSubject());
        jwtInfoToken.setUserId(claims.get("userId", String.class));
        return jwtInfoToken;
    }


    private Set<Role> getRoles(Claims claims) {
        try {
            List<String> roleNames = claims.get("roles", List.class);
            // Загружаем все роли одним запросом
            return roleService.findAllByNameIn(roleNames);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to parse roles from JWT claims", e);
        }
    }

}