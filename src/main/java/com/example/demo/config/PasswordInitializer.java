package com.example.demo.config;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PasswordInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public PasswordInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    @Transactional
    public void initializePasswords() {
        try {
            System.out.println("=== Starting password initialization ===");
            
            // Check if admin exists
            var adminOpt = userRepository.findByUsername("admin");
            if (adminOpt.isPresent()) {
                var admin = adminOpt.get();
                String encodedPassword = passwordEncoder.encode("admin123");
                admin.setPassword(encodedPassword);
                userRepository.save(admin);
                System.out.println("=== Admin password updated to BCrypt hash ===");
                System.out.println("Hash: " + encodedPassword);
            } else {
                System.err.println("=== WARNING: Admin user not found in database! ===");
                System.err.println("=== Creating admin user... ===");
                User admin = User.builder()
                        .username("admin")
                        .email("admin@example.com")
                        .password(passwordEncoder.encode("admin123"))
                        .role("ADMIN")
                        .build();
                userRepository.save(admin);
                System.out.println("=== Admin user created with password: admin123 ===");
            }

            // Check if john exists
            var johnOpt = userRepository.findByUsername("john");
            if (johnOpt.isPresent()) {
                var john = johnOpt.get();
                String encodedPassword = passwordEncoder.encode("john123");
                john.setPassword(encodedPassword);
                userRepository.save(john);
                System.out.println("=== John password updated to BCrypt hash ===");
                System.out.println("Hash: " + encodedPassword);
            } else {
                System.err.println("=== WARNING: John user not found in database! ===");
                System.err.println("=== Creating john user... ===");
                User john = User.builder()
                        .username("john")
                        .email("john@example.com")
                        .password(passwordEncoder.encode("john123"))
                        .role("USER")
                        .build();
                userRepository.save(john);
                System.out.println("=== John user created with password: john123 ===");
            }
            
            System.out.println("=== Password initialization completed ===");
        } catch (Exception e) {
            System.err.println("Error initializing passwords: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

