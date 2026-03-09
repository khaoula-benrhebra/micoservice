package com.microservice.userservice.service.impl;

import com.microservice.userservice.dto.CreateUserRequestDto;
import com.microservice.userservice.dto.UpdateUserRequestDto;
import com.microservice.userservice.dto.UserResponseDto;
import com.microservice.userservice.entity.User;
import com.microservice.userservice.exception.BadRequestException;
import com.microservice.userservice.exception.ResourceNotFoundException;
import com.microservice.userservice.mapper.UserMapper;
import com.microservice.userservice.repository.UserRepository;
import com.microservice.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto createUser(CreateUserRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new BadRequestException("Un utilisateur avec cet email existe deja");
        }

        User user = userMapper.toEntity(requestDto);
        User savedUser = userRepository.save(user);
        return userMapper.toResponseDto(savedUser);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponseDto)
                .toList();
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        User user = findUserById(id);
        return userMapper.toResponseDto(user);
    }

    @Override
    public UserResponseDto updateUser(Long id, UpdateUserRequestDto requestDto) {
        User user = findUserById(id);

        if (!user.getEmail().equals(requestDto.getEmail())
                && userRepository.existsByEmail(requestDto.getEmail())) {
            throw new BadRequestException("Un utilisateur avec cet email existe deja");
        }

        userMapper.updateEntityFromDto(requestDto, user);
        User updatedUser = userRepository.save(user);
        return userMapper.toResponseDto(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        User user = findUserById(id);
        userRepository.delete(user);
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable avec l'id : " + id));
    }
}
