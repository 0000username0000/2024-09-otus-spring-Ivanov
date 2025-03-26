package ru.otus.auth.services;

import jakarta.transaction.Transactional;
import ru.otus.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


import java.util.Set;
import java.util.stream.Collectors;

//@Service
@RequiredArgsConstructor
public class UsersService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Users user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
//
//        Set<GrantedAuthority> authorities = user.getRoles().stream()
//                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
//                .collect(Collectors.toSet());
//
//        return new User(
//                user.getUsername(),
//                user.getPassword(),
//                authorities
//        );
        return null;
    }

//    @Transactional
//    public Users save(Users users) {
//        return userRepository.save(users);
//    }

}
