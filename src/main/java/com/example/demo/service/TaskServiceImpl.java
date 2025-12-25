package com.example.demo.service;
import com.example.demo.dto.TaskCreateDto;
import com.example.demo.dto.TaskResponseDto;
import com.example.demo.dto.TaskUpdateDto;
import com.example.demo.entity.Tag;
import com.example.demo.entity.Task;
import com.example.demo.entity.User;
import com.example.demo.mapper.TaskMapper;
import com.example.demo.repository.TagRepository;
import com.example.demo.repository.TaskRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final TaskMapper taskMapper;

    @Override
    public List<TaskResponseDto> getAll() {
        log.debug("Retrieving all tasks");
        List<TaskResponseDto> tasks = taskRepository.findAll().stream()
                .map(taskMapper::toResponse)
                .toList();
        log.info("Retrieved {} tasks", tasks.size());
        return tasks;
    }

    @Override
    public TaskResponseDto getById(Long id) {
        log.debug("Retrieving task with id: {}", id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Task not found with id: {}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
                });
        log.info("Retrieved task with id: {}", id);
        return taskMapper.toResponse(task);
    }
    @Override
    public TaskResponseDto addTagToTask(Long taskId, Long tagId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Task not found"));

        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Tag not found"));

        List<Tag> tags = task.getTags();
        if (tags == null) {
            tags = new ArrayList<>();
            task.setTags(tags);
        }

        boolean alreadyExists = tags.stream()
                .anyMatch(t -> t.getId().equals(tagId));
        if (!alreadyExists) {
            tags.add(tag);
        }

        Task saved = taskRepository.save(task);
        return taskMapper.toResponse(saved);
    }

    @Override
    public TaskResponseDto removeTagFromTask(Long taskId, Long tagId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Task not found"));

        List<Tag> tags = task.getTags();
        if (tags != null) {
            tags.removeIf(t -> t.getId().equals(tagId));
        }

        Task saved = taskRepository.save(task);
        return taskMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public TaskResponseDto create(TaskCreateDto dto) {
        log.debug("Creating new task with title: {}", dto.getTitle());
        Task task = taskMapper.toEntity(dto);

        if (dto.getAuthorId() != null) {
            User author = userRepository.findById(dto.getAuthorId())
                    .orElseThrow(() -> {
                        log.warn("Author not found with id: {}", dto.getAuthorId());
                        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found");
                    });
            task.setAuthor(author);
        }

        if (dto.getTagIds() != null && !dto.getTagIds().isEmpty()) {
            List<Tag> tags = tagRepository.findAllById(dto.getTagIds());
            task.setTags(tags);
            log.debug("Added {} tags to task", tags.size());
        }

        LocalDateTime now = LocalDateTime.now();
        task.setCreatedAt(now);
        task.setUpdatedAt(now);

        Task saved = taskRepository.save(task);
        log.info("Created task with id: {} and title: {}", saved.getId(), saved.getTitle());
        return taskMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public TaskResponseDto update(Long id, TaskUpdateDto dto) {
        log.debug("Updating task with id: {}", id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Task not found with id: {}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
                });

        taskMapper.updateEntity(dto, task);

        if (dto.getAuthorId() != null) {
            User author = userRepository.findById(dto.getAuthorId())
                    .orElseThrow(() -> {
                        log.warn("Author not found with id: {}", dto.getAuthorId());
                        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found");
                    });
            task.setAuthor(author);
        }

        if (dto.getTagIds() != null) {
            List<Tag> tags = tagRepository.findAllById(dto.getTagIds());
            task.setTags(tags);
            log.debug("Updated tags for task, now has {} tags", tags.size());
        }

        task.setUpdatedAt(LocalDateTime.now());

        Task saved = taskRepository.save(task);
        log.info("Updated task with id: {}", id);
        return taskMapper.toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        log.debug("Deleting task with id: {}", id);
        if (!taskRepository.existsById(id)) {
            log.warn("Task not found with id: {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }
        taskRepository.deleteById(id);
        log.info("Deleted task with id: {}", id);
    }
}
