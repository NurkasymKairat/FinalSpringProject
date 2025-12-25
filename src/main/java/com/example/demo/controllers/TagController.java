package com.example.demo.controllers;

import com.example.demo.dto.TagDto;
import com.example.demo.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping
    public ResponseEntity<List<TagDto>> getAll() {
        log.debug("Getting all tags");
        List<TagDto> tags = tagService.getAll();
        log.info("Retrieved {} tags", tags.size());
        return ResponseEntity.ok(tags);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagDto> getById(@PathVariable Long id) {
        log.debug("Getting tag by id: {}", id);
        TagDto tag = tagService.getById(id);
        log.info("Retrieved tag with id: {}", id);
        return ResponseEntity.ok(tag);
    }

    @PostMapping
    public ResponseEntity<TagDto> create(@RequestBody TagDto tag) {
        log.debug("Creating new tag with name: {}", tag.getName());
        TagDto createdTag = tagService.create(tag);
        log.info("Created tag with id: {} and name: {}", createdTag.getId(), createdTag.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTag);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TagDto> update(@PathVariable Long id, @RequestBody TagDto tag) {
        log.debug("Updating tag with id: {}", id);
        TagDto updatedTag = tagService.update(id, tag);
        log.info("Updated tag with id: {}", id);
        return ResponseEntity.ok(updatedTag);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("Deleting tag with id: {}", id);
        tagService.delete(id);
        log.info("Deleted tag with id: {}", id);
        return ResponseEntity.noContent().build();
    }
}
