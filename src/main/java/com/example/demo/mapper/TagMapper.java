package com.example.demo.mapper;

import com.example.demo.dto.TagDto;
import com.example.demo.entity.Tag;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TagMapper {


    @Mapping(target = "tasks", ignore = true)
    Tag toEntity(TagDto dto);


    TagDto toResponse(Tag tag);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "tasks", ignore = true)
    void updateEntity(TagDto dto, @MappingTarget Tag tag);
}
