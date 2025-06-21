package org.example.mapper;

import org.example.dto.UserDto;
import org.example.model.User;

public class UserMapper {
    public static UserDto toDto(User user) {
        if (user == null) return null;
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge(),
                user.getCreatedAt()
        );
    }

    public static User toEntity(UserDto dto) {
        if (dto == null) return null;
        User user = new User(dto.getName(), dto.getEmail(), dto.getAge());
        user.setId(dto.getId());
        user.setCreatedAt(dto.getCreatedAt());
        return user;
    }
}