package com.example.demo.service;

import com.example.demo.dto.TaskCreateDto;
import com.example.demo.dto.TaskResponseDto;
import com.example.demo.dto.TaskUpdateDto;

import java.util.List;

public interface TaskService {

    List<TaskResponseDto> getAll();

    TaskResponseDto getById(Long id);

    TaskResponseDto create(TaskCreateDto dto);

    TaskResponseDto update(Long id, TaskUpdateDto dto);

    void delete(Long id);


    TaskResponseDto addTagToTask(Long taskId, Long tagId);

    TaskResponseDto removeTagFromTask(Long taskId, Long tagId);
}
