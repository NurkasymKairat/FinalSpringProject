package com.example.demo.controllers;

import com.example.demo.dto.TaskCreateDto;
import com.example.demo.dto.TaskResponseDto;
import com.example.demo.dto.TaskUpdateDto;
import com.example.demo.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> getAll() {
        log.debug("Getting all tasks");
        List<TaskResponseDto> tasks = taskService.getAll();
        log.info("Retrieved {} tasks", tasks.size());
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getById(@PathVariable Long id) {
        log.debug("Getting task by id: {}", id);
        TaskResponseDto task = taskService.getById(id);
        log.info("Retrieved task with id: {}", id);
        return ResponseEntity.ok(task);
    }

    @PostMapping
    public ResponseEntity<TaskResponseDto> create(@RequestBody TaskCreateDto dto) {
        log.debug("Creating new task with title: {}", dto.getTitle());
        TaskResponseDto createdTask = taskService.create(dto);
        log.info("Created task with id: {}", createdTask.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> update(@PathVariable Long id,
                                                   @RequestBody TaskUpdateDto dto) {
        log.debug("Updating task with id: {}", id);
        TaskResponseDto updatedTask = taskService.update(id, dto);
        log.info("Updated task with id: {}", id);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("Deleting task with id: {}", id);
        taskService.delete(id);
        log.info("Deleted task with id: {}", id);
        return ResponseEntity.noContent().build();
    }
}

