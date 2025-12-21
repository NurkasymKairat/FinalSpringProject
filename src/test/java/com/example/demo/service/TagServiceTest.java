package com.example.demo.service;

import com.example.demo.dto.TagDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Random;

@SpringBootTest
public class TagServiceTest {

    @Autowired
    private TagService tagService;

    @Test
    void getAllTest() {
        List<TagDto> tagDtos = tagService.getAll();
        
        Assertions.assertNotNull(tagDtos);
        Assertions.assertNotEquals(0, tagDtos.size());
        
        for (int i = 0; i < tagDtos.size(); i++) {
            TagDto tagDto = tagDtos.get(i);
            Assertions.assertNotNull(tagDto.getId());
            Assertions.assertNotNull(tagDto.getName());
        }
    }

    @Test
    void getByIdTest() {
        Random random = new Random();
        System.out.println(tagService.getAll().size());
        
        if (tagService.getAll().size() > 0) {
            int randomIndex = random.nextInt(tagService.getAll().size());
            Long someIndex = tagService.getAll().get(randomIndex).getId();
            
            TagDto tagDto = tagService.getById(someIndex);
            
            Assertions.assertNotNull(tagDto);
            Assertions.assertNotNull(tagDto.getId());
            Assertions.assertNotNull(tagDto.getName());
        }
    }

    @Test
    void createTest() {
        TagDto tag = new TagDto();
        tag.setName("test-tag-" + System.currentTimeMillis());
        
        TagDto createdTag = tagService.create(tag);
        
        Assertions.assertNotNull(createdTag);
        Assertions.assertNotNull(createdTag.getId());
        Assertions.assertNotNull(createdTag.getName());
        Assertions.assertEquals(tag.getName(), createdTag.getName());
        
        TagDto dto = tagService.getById(createdTag.getId());
        
        Assertions.assertNotNull(dto);
        Assertions.assertNotNull(dto.getId());
        Assertions.assertNotNull(dto.getName());
        Assertions.assertEquals(createdTag.getId(), dto.getId());
        Assertions.assertEquals(createdTag.getName(), dto.getName());
    }

    @Test
    void updateTest() {
        long timestamp1 = System.currentTimeMillis();
        TagDto tag = new TagDto();
        tag.setName("update-test-" + timestamp1);
        
        TagDto createdTag = tagService.create(tag);
        Long someIndex = createdTag.getId();
        
        long timestamp2 = System.currentTimeMillis() + 1;
        TagDto updateDto = new TagDto();
        updateDto.setName("updated-" + timestamp2);
        
        TagDto updated = tagService.update(someIndex, updateDto);
        
        Assertions.assertNotNull(updated);
        Assertions.assertNotNull(updated.getId());
        Assertions.assertNotNull(updated.getName());
        
        TagDto changedTag = tagService.getById(someIndex);
        
        Assertions.assertNotNull(changedTag);
        Assertions.assertNotNull(changedTag.getId());
        Assertions.assertNotNull(changedTag.getName());
        Assertions.assertEquals(updated.getId(), changedTag.getId());
        Assertions.assertEquals(updated.getName(), changedTag.getName());
    }

    @Test
    void deleteTest() {
        TagDto tag = new TagDto();
        tag.setName("delete-test-" + System.currentTimeMillis());
        
        TagDto createdTag = tagService.create(tag);
        Long someIndex = createdTag.getId();
        
        tagService.delete(someIndex);
        
        try {
            tagService.getById(someIndex);
            Assertions.fail("Tag should be deleted");
        } catch (Exception e) {
            Assertions.assertTrue(true);
        }
    }
}

