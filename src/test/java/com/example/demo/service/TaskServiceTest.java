package com.example.demo.service;

import com.example.demo.dto.TaskCreateDto;
import com.example.demo.dto.TaskResponseDto;
import com.example.demo.dto.TaskUpdateDto;
import com.example.demo.dto.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Random;

@SpringBootTest
public class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @Test
    void getAllTest() {
        List<TaskResponseDto> taskDtos = taskService.getAll();
        
        Assertions.assertNotNull(taskDtos);
        Assertions.assertNotEquals(0, taskDtos.size());
        
        for (int i = 0; i < taskDtos.size(); i++) {
            TaskResponseDto taskDto = taskDtos.get(i);
            Assertions.assertNotNull(taskDto.getId());
            Assertions.assertNotNull(taskDto.getTitle());
            Assertions.assertNotNull(taskDto.getStatus());
        }
    }

    @Test
    void getByIdTest() {
        Random random = new Random();
        System.out.println(taskService.getAll().size());
        
        if (taskService.getAll().size() > 0) {
            int randomIndex = random.nextInt(taskService.getAll().size());
            Long someIndex = taskService.getAll().get(randomIndex).getId();
            
            TaskResponseDto taskDto = taskService.getById(someIndex);
            
            Assertions.assertNotNull(taskDto);
            Assertions.assertNotNull(taskDto.getId());
            Assertions.assertNotNull(taskDto.getTitle());
            Assertions.assertNotNull(taskDto.getStatus());
        }
    }

    @Test
    void createTest() {
        UserDto user = new UserDto();
        long timestamp = System.currentTimeMillis();
        user.setUsername("task-user-" + timestamp);
        user.setEmail("task-" + timestamp + "@example.com");
        user.setPassword("password123");
        UserDto createdUser = userService.create(user);
        
        TaskCreateDto task = new TaskCreateDto();
        task.setTitle("Test Task " + timestamp);
        task.setDescription("Test descr");
        task.setStatus("pending");
        task.setAuthorId(createdUser.getId());
        
        TaskResponseDto createdTask = taskService.create(task);
        
        Assertions.assertNotNull(createdTask);
        Assertions.assertNotNull(createdTask.getId());
        Assertions.assertNotNull(createdTask.getTitle());
        Assertions.assertNotNull(createdTask.getStatus());
        Assertions.assertEquals(task.getTitle(), createdTask.getTitle());
        Assertions.assertEquals(task.getStatus(), createdTask.getStatus());
        
        TaskResponseDto dto = taskService.getById(createdTask.getId());
        
        Assertions.assertNotNull(dto);
        Assertions.assertNotNull(dto.getId());
        Assertions.assertNotNull(dto.getTitle());
        Assertions.assertNotNull(dto.getStatus());
        Assertions.assertEquals(createdTask.getId(), dto.getId());
        Assertions.assertEquals(createdTask.getTitle(), dto.getTitle());
        Assertions.assertEquals(createdTask.getStatus(), dto.getStatus());
    }

    @Test
    void updateTest() {
        Random random = new Random();
        
        if (taskService.getAll().size() > 0) {
            int randomIndex = random.nextInt(taskService.getAll().size());
            Long someIndex = taskService.getAll().get(randomIndex).getId();
            
            TaskUpdateDto task = new TaskUpdateDto();
            task.setTitle("Updated Task");
            task.setDescription("Updated descr");
            task.setStatus("progress");
            
            TaskResponseDto updated = taskService.update(someIndex, task);
            
            Assertions.assertNotNull(updated);
            Assertions.assertNotNull(updated.getId());
            Assertions.assertNotNull(updated.getTitle());
            Assertions.assertNotNull(updated.getStatus());
            
            TaskResponseDto changedTask = taskService.getById(someIndex);
            
            Assertions.assertNotNull(changedTask);
            Assertions.assertNotNull(changedTask.getId());
            Assertions.assertNotNull(changedTask.getTitle());
            Assertions.assertNotNull(changedTask.getStatus());
            Assertions.assertEquals(updated.getId(), changedTask.getId());
            Assertions.assertEquals(updated.getTitle(), changedTask.getTitle());
            Assertions.assertEquals(updated.getStatus(), changedTask.getStatus());
        }
    }

    @Test
    void deleteTest() {
        UserDto user = new UserDto();
        long timestamp = System.currentTimeMillis();
        user.setUsername("delete-task-user-" + timestamp);
        user.setEmail("delete-task-" + timestamp + "@example.com");
        user.setPassword("password123");
        UserDto createdUser = userService.create(user);
        
        TaskCreateDto task = new TaskCreateDto();
        task.setTitle("Delete Test Task " + timestamp);
        task.setDescription("Test descr");
        task.setStatus("pending");
        task.setAuthorId(createdUser.getId());
        
        TaskResponseDto createdTask = taskService.create(task);
        Long someIndex = createdTask.getId();
        
        taskService.delete(someIndex);
        
        try {
            taskService.getById(someIndex);
            Assertions.fail("Task should be deleted");
        } catch (Exception e) {
            Assertions.assertTrue(true);
        }
    }
}

