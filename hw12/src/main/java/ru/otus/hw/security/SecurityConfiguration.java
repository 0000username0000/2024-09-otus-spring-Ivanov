package ru.otus.hw.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.otus.hw.services.UsersService;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    private final UsersService usersService;

    public SecurityConfiguration(UsersService usersService) {
        this.usersService = usersService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/main", "/genres", "/books/*",
                                "/book-editor", "/book-create", "/authors").authenticated()
                        .anyRequest().permitAll())
                .anonymous(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .formLogin(formLogin -> formLogin
                        .defaultSuccessUrl("/main", true)
                        .failureUrl("/fail"))
                .userDetailsService(usersService);

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
