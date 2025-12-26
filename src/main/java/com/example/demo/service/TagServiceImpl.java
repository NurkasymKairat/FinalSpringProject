package com.example.demo.service;

import com.example.demo.dto.TagDto;
import com.example.demo.entity.Tag;
import com.example.demo.mapper.TagMapper;
import com.example.demo.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository repository;
    private final TagMapper mapper;

    @Override
    public TagDto create(TagDto dto) {
        log.debug("Creating new tag with name: {}", dto.getName());
        Tag tag = mapper.toEntity(dto);
        repository.save(tag);
        log.info("Created tag with id: {} and name: {}", tag.getId(), tag.getName());
        return mapper.toDto(tag);
    }

    @Override
    public TagDto update(Long id, TagDto dto) {
        log.debug("Updating tag with id: {}", id);
        Tag tag = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Tag not found with id: {}", id);
                    return new RuntimeException("Tag not found");
                });

        mapper.updateEntity(dto, tag);
        repository.save(tag);
        log.info("Updated tag with id: {}", id);
        return mapper.toDto(tag);
    }

    @Override
    public TagDto getById(Long id) {
        log.debug("Retrieving tag with id: {}", id);
        TagDto tag = repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> {
                    log.warn("Tag not found with id: {}", id);
                    return new RuntimeException("Tag not found");
                });
        log.info("Retrieved tag with id: {}", id);
        return tag;
    }

    @Override
    public List<TagDto> getAll() {
        log.debug("Retrieving all tags");
        List<TagDto> tags = repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
        log.info("Retrieved {} tags", tags.size());
        return tags;
    }

    @Override
    public void delete(Long id) {
        log.debug("Deleting tag with id: {}", id);
        repository.deleteById(id);
        log.info("Deleted tag with id: {}", id);
    }
}
