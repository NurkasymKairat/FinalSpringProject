package com.example.demo.config;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PasswordInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        createUserIfNotExists("admin", "admin@example.com", "ADMIN", "admin123");
        createUserIfNotExists("john", "john@example.com", "USER", "john123");
    }

    private void createUserIfNotExists(
            String username,
            String email,
            String role,
            String rawPassword
    ) {
        userRepository.findByUsername(username).ifPresentOrElse(
                user -> log.info("User '{}' already exists", username),
                () -> {
                    User user = User.builder()
                            .username(username)
                            .email(email)
                            .password(passwordEncoder.encode(rawPassword))
                            .role(role)
                            .build();
                    userRepository.save(user);
                    log.info("User '{}' created", username);
                }
        );
    }
}
