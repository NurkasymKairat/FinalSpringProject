package com.example.demo.config;

import com.example.demo.repository.UserRepository;
import com.example.demo.service.MyUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public MyUserService myUserService(UserRepository userRepository) {
        return new MyUserService(userRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, MyUserService myUserService, PasswordEncoder passwordEncoder) throws Exception {
        AuthenticationManagerBuilder authBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authBuilder
                .userDetailsService(myUserService)
                .passwordEncoder(passwordEncoder);

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.GET).permitAll()
                .requestMatchers(HttpMethod.POST).hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT).hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE).hasRole("ADMIN")
                .anyRequest().authenticated()
        );
        http.httpBasic(Customizer.withDefaults());
        http.csrf(csrf -> csrf.disable());

        return http.build();
    }
}
