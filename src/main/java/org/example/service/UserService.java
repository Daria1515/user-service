package org.example.service;

import org.example.dto.UserDto;
import org.example.mapper.UserMapper;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<UserDto> getAllUsers() {
        return repository.findAll().stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Long id) {
        return repository.findById(id).map(UserMapper::toDto).orElse(null);
    }

    public UserDto createUser(UserDto dto) {
        User user = UserMapper.toEntity(dto);
        return UserMapper.toDto(repository.save(user));
    }

    public UserDto updateUser(Long id, UserDto dto) {
        User user = repository.findById(id).orElse(null);
        if (user == null) return null;
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setAge(dto.getAge());
        return UserMapper.toDto(repository.save(user));
    }

    public boolean deleteUser(Long id) {
        if (!repository.existsById(id)) return false;
        repository.deleteById(id);
        return true;
    }
}
