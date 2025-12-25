package com.example.demo.controllers;

import com.example.demo.dto.UserDto;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAll() {
        log.debug("Getting all users");
        List<UserDto> users = userService.getAll();
        log.info("Retrieved {} users", users.size());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable Long id) {
        log.debug("Getting user by id: {}", id);
        UserDto user = userService.getById(id);
        log.info("Retrieved user with id: {}", id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody UserDto dto) {
        log.debug("Creating new user with username: {}", dto.getUsername());
        UserDto createdUser = userService.create(dto);
        log.info("Created user with id: {} and username: {}", createdUser.getId(), createdUser.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable Long id, @RequestBody UserDto dto) {
        log.debug("Updating user with id: {}", id);
        UserDto updatedUser = userService.update(id, dto);
        log.info("Updated user with id: {}", id);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("Deleting user with id: {}", id);
        userService.delete(id);
        log.info("Deleted user with id: {}", id);
        return ResponseEntity.noContent().build();
    }
}
