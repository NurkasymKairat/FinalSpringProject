package com.example.demo.dto;

import lombok.Data;

import java.util.List;

@Data
public class TaskCreateDto {

    private String title;
    private String description;
    private String status;

    private Long authorId;

    private List<Long> tagIds;
}
