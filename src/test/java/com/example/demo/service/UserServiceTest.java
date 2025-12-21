package com.example.demo.service;

import com.example.demo.dto.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Random;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void getAllTest() {
        List<UserDto> userDtos = userService.getAll();
        
        Assertions.assertNotNull(userDtos);
        Assertions.assertNotEquals(0, userDtos.size());
        
        for (int i = 0; i < userDtos.size(); i++) {
            UserDto userDto = userDtos.get(i);
            Assertions.assertNotNull(userDto.getId());
            Assertions.assertNotNull(userDto.getUsername());
            Assertions.assertNotNull(userDto.getEmail());
        }
    }

    @Test
    void getByIdTest() {
        Random random = new Random();
        System.out.println(userService.getAll().size());
        
        if (userService.getAll().size() > 0) {
            int randomIndex = random.nextInt(userService.getAll().size());
            Long someIndex = userService.getAll().get(randomIndex).getId();
            
            UserDto userDto = userService.getById(someIndex);
            
            Assertions.assertNotNull(userDto);
            Assertions.assertNotNull(userDto.getId());
            Assertions.assertNotNull(userDto.getUsername());
            Assertions.assertNotNull(userDto.getEmail());
        }
    }

    @Test
    void createTest() {
        UserDto user = new UserDto();
        long timestamp = System.currentTimeMillis();
        user.setUsername("testuser-" + timestamp);
        user.setEmail("test-" + timestamp + "@example.com");
        user.setPassword("password123");
        
        UserDto createdUser = userService.create(user);
        
        Assertions.assertNotNull(createdUser);
        Assertions.assertNotNull(createdUser.getId());
        Assertions.assertNotNull(createdUser.getUsername());
        Assertions.assertNotNull(createdUser.getEmail());
        Assertions.assertEquals(user.getUsername(), createdUser.getUsername());
        Assertions.assertEquals(user.getEmail(), createdUser.getEmail());
        
        UserDto dto = userService.getById(createdUser.getId());
        
        Assertions.assertNotNull(dto);
        Assertions.assertNotNull(dto.getId());
        Assertions.assertNotNull(dto.getUsername());
        Assertions.assertNotNull(dto.getEmail());
        Assertions.assertEquals(createdUser.getId(), dto.getId());
        Assertions.assertEquals(createdUser.getUsername(), dto.getUsername());
        Assertions.assertEquals(createdUser.getEmail(), dto.getEmail());
    }

    @Test
    void updateTest() {
        UserDto user = new UserDto();
        long timestamp = System.currentTimeMillis();
        user.setUsername("update-user-" + timestamp);
        user.setEmail("update-" + timestamp + "@example.com");
        user.setPassword("password123");
        
        UserDto createdUser = userService.create(user);
        Long someIndex = createdUser.getId();
        
        UserDto updateDto = new UserDto();
        long updateTimestamp = System.currentTimeMillis();
        updateDto.setUsername("updated-user-" + updateTimestamp);
        updateDto.setEmail("updated-" + updateTimestamp + "@example.com");
        updateDto.setPassword("newpassword");
        
        UserDto updated = userService.update(someIndex, updateDto);
        
        Assertions.assertNotNull(updated);
        Assertions.assertNotNull(updated.getId());
        Assertions.assertNotNull(updated.getUsername());
        Assertions.assertNotNull(updated.getEmail());
        
        UserDto changedUser = userService.getById(someIndex);
        
        Assertions.assertNotNull(changedUser);
        Assertions.assertNotNull(changedUser.getId());
        Assertions.assertNotNull(changedUser.getUsername());
        Assertions.assertNotNull(changedUser.getEmail());
        Assertions.assertEquals(updated.getId(), changedUser.getId());
        Assertions.assertEquals(updated.getUsername(), changedUser.getUsername());
        Assertions.assertEquals(updated.getEmail(), changedUser.getEmail());
    }

    @Test
    void deleteTest() {
        UserDto user = new UserDto();
        long timestamp = System.currentTimeMillis();
        user.setUsername("delete-user-" + timestamp);
        user.setEmail("delete-" + timestamp + "@example.com");
        user.setPassword("password123");
        
        UserDto createdUser = userService.create(user);
        Long someIndex = createdUser.getId();
        
        userService.delete(someIndex);
        
        try {
            userService.getById(someIndex);
            Assertions.fail("User should be deleted");
        } catch (Exception e) {
            Assertions.assertTrue(true);
        }
    }
}

