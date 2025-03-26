package ru.otus.auth.services;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import ru.otus.auth.models.User;
import ru.otus.auth.models.Role;
import ru.otus.auth.repository.UserRepository;

import java.util.Collections;
import java.util.List;

@Service
//@RequiredArgsConstructor
public class UserService {

//    private final List<User> users;

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
//        this.users = List.of(
//                new User("anton", "1234", "Антон", "Иванов", Collections.singleton(Role.USER)),
//                new User("ivan", "12345", "Сергей", "Петров", Collections.singleton(Role.ADMIN))
//        );
    }

//    @PostConstruct
//    public void init() {
//        userRepository.saveAll(List.of(
//                User.builder().login("anton").password("1234").firstName("Антон").lastName("Иванов").roles("USER").build(),
//                User.builder().login("ivan").password("12345").firstName("Сергей").lastName("Петров").roles(Collections.singleton(Role.ADMIN)).build()
//        ));
//    }

    public User getByLogin(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }
}