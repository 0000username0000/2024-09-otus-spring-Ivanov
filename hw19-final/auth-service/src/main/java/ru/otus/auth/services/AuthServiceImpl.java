package ru.otus.auth.services;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.otus.auth.components.JwtProvider;
import ru.otus.auth.dto.JwtRequest;
import ru.otus.auth.dto.JwtResponse;
import ru.otus.auth.dto.UserInfoResponse;
import ru.otus.auth.exceptions.InvalidCredentialsException;
import ru.otus.auth.exceptions.InvalidTokenException;
import ru.otus.auth.models.JwtAuthentication;
import ru.otus.auth.models.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;

    private final Map<String, String> refreshStorage = new HashMap<>();

    private final JwtProvider jwtProvider;

    @Override
    public JwtResponse login(@NonNull JwtRequest authRequest) {
        User user = userService.getByLoginNN(authRequest.getLogin());
        if (!userService.validatePassword(authRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid password");
        }
        String accessToken = jwtProvider.generateAccessToken(user);
        String refreshToken = jwtProvider.generateRefreshToken(user);
        refreshStorage.put(user.getLogin(), refreshToken);
        return new JwtResponse(accessToken, refreshToken, userService.isAdmin(user));
    }

    @Override
    public JwtResponse getAccessToken(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            String login = claims.getSubject();
            String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                User user = userService.getByLoginNN(login);
                String accessToken = jwtProvider.generateAccessToken(user);
                return new JwtResponse(accessToken, null, userService.isAdmin(user));
            }
        }
        return new JwtResponse(null, null, null);
    }

    @Override
    public JwtResponse refresh(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            String login = claims.getSubject();
            String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                User user = userService.getByLoginNN(login);
                String accessToken = jwtProvider.generateAccessToken(user);
                String newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshStorage.put(user.getLogin(), newRefreshToken);
                return new JwtResponse(accessToken, newRefreshToken, userService.isAdmin(user));
            }
        }
        throw new InvalidTokenException("Invalid refresh token");
    }

    @Override
    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public UserInfoResponse getUserInfo(String token) {
        Claims claims = jwtProvider.getAccessClaims(token);
        return new UserInfoResponse(
                claims.getSubject(),
                claims.get("roles", List.class),
                claims.get("firstName", String.class),
                claims.get("userId", String.class)
        );
    }
}
