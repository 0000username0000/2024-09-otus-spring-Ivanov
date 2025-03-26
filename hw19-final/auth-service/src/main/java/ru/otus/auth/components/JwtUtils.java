package ru.otus.auth.components;

import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.auth.models.JwtAuthentication;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import ru.otus.auth.models.Role;
import ru.otus.auth.services.RoleService;

//@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Component
@AllArgsConstructor
public final class JwtUtils {

    private final RoleService roleService;

    public JwtAuthentication generate(Claims claims) {
        final JwtAuthentication jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setRoles(getRoles(claims));
        jwtInfoToken.setFirstName(claims.get("firstName", String.class));
        jwtInfoToken.setUsername(claims.getSubject());
        return jwtInfoToken;
    }

//    private Set<Role> getRoles(Claims claims) {
//        try {
//            // Получаем роли как список строк
//            List<String> roleNames = claims.get("roles", List.class);
//            return roleNames.stream()
//                    .map(roleService::findByNameNN)
//                    .collect(Collectors.toSet());
//        } catch (Exception e) {
//            throw new IllegalArgumentException("Failed to parse roles from JWT claims", e);
//        }
//    }

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