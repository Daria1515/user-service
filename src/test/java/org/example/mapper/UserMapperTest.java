package org.example.mapper;

import org.example.dto.UserDto;
import org.example.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    @Test
    @DisplayName("toDto: корректное преобразование User -> UserDto")
    void testToDto() {
        LocalDateTime now = LocalDateTime.now();
        User user = new User("Alice", "alice@example.com", 30);
        user.setId(1L);
        user.setCreatedAt(now);

        UserDto dto = UserMapper.toDto(user);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Alice", dto.getName());
        assertEquals("alice@example.com", dto.getEmail());
        assertEquals(30, dto.getAge());
        assertEquals(now, dto.getCreatedAt());
    }

    @Test
    @DisplayName("toEntity: корректное преобразование UserDto -> User")
    void testToEntity() {
        LocalDateTime now = LocalDateTime.now();
        UserDto dto = new UserDto(2L, "Bob", "bob@example.com", 40, now);

        User user = UserMapper.toEntity(dto);

        assertNotNull(user);
        assertEquals(2L, user.getId());
        assertEquals("Bob", user.getName());
        assertEquals("bob@example.com", user.getEmail());
        assertEquals(40, user.getAge());
        assertEquals(now, user.getCreatedAt());
    }

    @Test
    @DisplayName("toDto: null -> null")
    void testToDto_Null() {
        assertNull(UserMapper.toDto(null));
    }

    @Test
    @DisplayName("toEntity: null -> null")
    void testToEntity_Null() {
        assertNull(UserMapper.toEntity(null));
    }
}
