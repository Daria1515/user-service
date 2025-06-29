package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.UserDto;
import org.example.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Создание пользователя")
    void testCreateUser() throws Exception {
        UserDto request = new UserDto(null, "Alice", "alice@example.com", 25, null);
        UserDto response = new UserDto(1L, "Alice", "alice@example.com", 25, null);

        Mockito.when(userService.createUser(any(UserDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Alice"));
    }

    @Test
    @DisplayName("Получение пользователя по id")
    void testGetUserById() throws Exception {
        UserDto user = new UserDto(1L, "Bob", "bob@example.com", 30, null);

        Mockito.when(userService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Bob"))
                .andExpect(jsonPath("$.email").value("bob@example.com"));
    }

    @Test
    @DisplayName("Получение всех пользователей")
    void testGetAllUsers() throws Exception {
        List<UserDto> users = List.of(
                new UserDto(1L, "Alice", "alice@example.com", 25, null),
                new UserDto(2L, "Bob", "bob@example.com", 30, null)
        );

        Mockito.when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    @DisplayName("Обновление пользователя")
    void testUpdateUser() throws Exception {
        UserDto request = new UserDto(null, "Updated", "new@example.com", 22, null);
        UserDto response = new UserDto(1L, "Updated", "new@example.com", 22, null);

        Mockito.when(userService.updateUser(eq(1L), any(UserDto.class))).thenReturn(response);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    @DisplayName("Удаление пользователя")
    void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Валидация: имя не должно быть пустым")
    void testCreateUser_InvalidName() throws Exception {
        UserDto request = new UserDto(null, "", "alice@example.com", 25, null);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.name").value("Имя не должно быть пустым"));
    }

    @Test
    @DisplayName("Пользователь не найден")
    void testUserNotFound() throws Exception {
        Mockito.when(userService.getUserById(999L))
                .thenThrow(new org.example.exception.UserNotFoundException(999L));

        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User not found"));
    }

    @Test
    @DisplayName("Обновление несуществующего пользователя")
    void testUpdateUserNotFound() throws Exception {
        UserDto request = new UserDto(null, "Ghost", "ghost@example.com", 100, null);

        Mockito.when(userService.updateUser(eq(999L), any(UserDto.class))).thenReturn(null);

        mockMvc.perform(put("/api/users/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Удаление несуществующего пользователя")
    void testDeleteUserNotFound() throws Exception {
        mockMvc.perform(delete("/api/users/999"))
                .andExpect(status().isNoContent());
    }


}
