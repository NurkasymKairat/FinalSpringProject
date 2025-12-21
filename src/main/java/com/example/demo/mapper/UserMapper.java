package com.example.demo.mapper;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    User toEntity(UserDto dto);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "tasks", ignore = true)
    void updateEntity(UserDto dto, @MappingTarget User user);


    @Mapping(target = "password", ignore = true)
    UserDto toResponse(User user);

}
