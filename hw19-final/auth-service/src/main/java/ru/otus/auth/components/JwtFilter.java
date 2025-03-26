package ru.otus.auth.components;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import ru.otus.auth.models.JwtAuthentication;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    private static final String AUTHORIZATION = "Authorization";

    private final JwtProvider jwtProvider;

    private final JwtUtils jwtUtils;

//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc)
//            throws IOException, ServletException {
//        final String token = getTokenFromRequest((HttpServletRequest) request);
//        if (token != null && jwtProvider.validateAccessToken(token)) {
//            final Claims claims = jwtProvider.getAccessClaims(token);
//            final JwtAuthentication jwtInfoToken = JwtUtils.generate(claims);
//            jwtInfoToken.setAuthenticated(true);
//            SecurityContextHolder.getContext().setAuthentication(jwtInfoToken);
//        }
//        fc.doFilter(request, response);
//    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // Пробуем получить токен из куки
        String token = Arrays.stream(httpRequest.getCookies())
                .filter(c -> "token".equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);

        // Если токен не найден в куки, проверяем заголовок Authorization
        if (token == null) {
            token = getTokenFromRequest(httpRequest);
        }

        if (token != null && jwtProvider.validateAccessToken(token)) {
            Claims claims = jwtProvider.getAccessClaims(token);
            JwtAuthentication auth = jwtUtils.generate(claims);
            auth.setAuthenticated(true);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        chain.doFilter(request, response);
    }


    private String getTokenFromRequest(HttpServletRequest request) {
        final String bearer = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

}
