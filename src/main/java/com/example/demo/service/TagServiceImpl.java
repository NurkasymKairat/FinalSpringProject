package com.example.demo.service;

import com.example.demo.dto.TagDto;
import com.example.demo.entity.Tag;
import com.example.demo.mapper.TagMapper;
import com.example.demo.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository repository;
    private final TagMapper mapper;

    @Override
    public TagDto create(TagDto dto) {
        Tag tag = mapper.toEntity(dto);
        repository.save(tag);
        return mapper.toResponse(tag);
    }

    @Override
    public TagDto update(Long id, TagDto dto) {
        Tag tag = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found"));

        mapper.updateEntity(dto, tag);
        repository.save(tag);

        return mapper.toResponse(tag);
    }

    @Override
    public TagDto getById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Tag not found"));
    }

    @Override
    public List<TagDto> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
