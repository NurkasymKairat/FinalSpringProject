package com.example.demo.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final PasswordEncoder passwordEncoder;

    public AuthController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/hash")
    public ResponseEntity<String> hashPassword(@RequestParam String password) {
        log.debug("Hashing password");
        String hashedPassword = passwordEncoder.encode(password);
        log.info("Password hashed successfully");
        return ResponseEntity.ok(hashedPassword);
    }

    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> testAuth() {
        log.debug("Testing authentication");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> result = new HashMap<>();
        result.put("authenticated", auth != null && auth.isAuthenticated());
        result.put("username", auth != null ? auth.getName() : "anonymous");
        result.put("authorities", auth != null ? auth.getAuthorities() : "none");
        log.info("Authentication test - user: {}, authenticated: {}", 
                result.get("username"), result.get("authenticated"));
        return ResponseEntity.ok(result);
    }
}

