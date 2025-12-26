package com.example.demo.service;

import com.example.demo.dto.TagDto;

import java.util.List;

public interface TagService {
    TagDto create(TagDto dto);

    TagDto update(Long id, TagDto dto);

    TagDto getById(Long id);

    List<TagDto> getAll();

    void delete(Long id);
}
