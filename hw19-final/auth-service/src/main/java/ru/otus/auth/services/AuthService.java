package ru.otus.auth.services;

import org.springframework.lang.NonNull;
import ru.otus.auth.dto.JwtRequest;
import ru.otus.auth.dto.JwtResponse;
import ru.otus.auth.dto.UserInfoResponse;
import ru.otus.auth.models.JwtAuthentication;


public interface AuthService {

    JwtResponse login(@NonNull JwtRequest authRequest);

    JwtResponse getAccessToken(@NonNull String refreshToken);

    JwtResponse refresh(@NonNull String refreshToken);

    JwtAuthentication getAuthInfo();

    UserInfoResponse getUserInfo(String token);
}
