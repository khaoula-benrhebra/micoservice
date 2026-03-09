package com.microservice.userservice.mapper;

import com.microservice.userservice.dto.CreateUserRequestDto;
import com.microservice.userservice.dto.UpdateUserRequestDto;
import com.microservice.userservice.dto.UserResponseDto;
import com.microservice.userservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(CreateUserRequestDto requestDto);

    UserResponseDto toResponseDto(User user);

    void updateEntityFromDto(UpdateUserRequestDto requestDto, @MappingTarget User user);
}
