package com.example.demo.mapper;

import com.example.demo.dto.TaskCreateDto;
import com.example.demo.dto.TaskResponseDto;
import com.example.demo.dto.TaskUpdateDto;
import com.example.demo.entity.Tag;
import com.example.demo.entity.Task;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Task toEntity(TaskCreateDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(TaskUpdateDto dto, @MappingTarget Task task);

    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "tagIds", expression = "java(toTagIdList(task.getTags()))")
    TaskResponseDto toResponse(Task task);

    default List<Long> toTagIdList(List<Tag> tags) {
        if (tags == null) return Collections.emptyList();
        List<Long> ids = new ArrayList<>();
        for (Tag tag : tags) {
            ids.add(tag.getId());
        }
        return ids;
    }
}

