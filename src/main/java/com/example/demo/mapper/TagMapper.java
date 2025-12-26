package com.example.demo.mapper;

import com.example.demo.dto.TagDto;
import com.example.demo.entity.Tag;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TagMapper {

    TagDto toDto(Tag tag);

    @Mapping(target = "tasks", ignore = true)
    Tag toEntity(TagDto dto);

    List<TagDto> toDtoList(List<Tag> tags);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "tasks", ignore = true)
    void updateEntity(TagDto dto, @MappingTarget Tag tag);
}
