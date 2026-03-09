package com.microservice.userservice.service;

import com.microservice.userservice.dto.CreateUserRequestDto;
import com.microservice.userservice.dto.UpdateUserRequestDto;
import com.microservice.userservice.dto.UserResponseDto;

import java.util.List;

public interface UserService {

    UserResponseDto createUser(CreateUserRequestDto requestDto);

    List<UserResponseDto> getAllUsers();

    UserResponseDto getUserById(Long id);

    UserResponseDto updateUser(Long id, UpdateUserRequestDto requestDto);

    void deleteUser(Long id);
}
