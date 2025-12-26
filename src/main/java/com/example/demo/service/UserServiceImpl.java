package com.example.demo.service;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserDto> getAll() {
        log.debug("Retrieving all users");
        List<UserDto> users = userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
        log.info("Retrieved {} users", users.size());
        return users;
    }

    @Override
    public UserDto getById(Long id) {
        log.debug("Retrieving user with id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User not found with id: {}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
                });
        log.info("Retrieved user with id: {}", id);
        return userMapper.toDto(user);
    }

    @Override
    public UserDto create(UserDto dto) {
        log.debug("Creating new user with username: {}", dto.getUsername());
        User user = userMapper.toEntity(dto);
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            log.debug("Password encoded for user: {}", dto.getUsername());
        }
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER");
            log.debug("Default role USER assigned to user: {}", dto.getUsername());
        }
        User saved = userRepository.save(user);
        log.info("Created user with id: {} and username: {}", saved.getId(), saved.getUsername());
        return userMapper.toDto(saved);
    }

    @Override
    public UserDto update(Long id, UserDto dto) {
        log.debug("Updating user with id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User not found with id: {}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
                });

        userMapper.updateEntity(dto, user);
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            log.debug("Password updated for user with id: {}", id);
        }
        User saved = userRepository.save(user);
        log.info("Updated user with id: {}", id);
        return userMapper.toDto(saved);
    }

    @Override
    public void delete(Long id) {
        log.debug("Deleting user with id: {}", id);
        if (!userRepository.existsById(id)) {
            log.warn("User not found with id: {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        userRepository.deleteById(id);
        log.info("Deleted user with id: {}", id);
    }
}
