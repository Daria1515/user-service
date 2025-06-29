package org.example.controller;

import org.example.dto.UserDto;
import org.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerHateoasTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void testGetAllUsersWithHateoas() throws Exception {
        UserDto user1 = new UserDto(1L, "Иван Иванов", "ivan@example.com", 25, LocalDateTime.now());
        UserDto user2 = new UserDto(2L, "Мария Петрова", "maria@example.com", 30, LocalDateTime.now());

        when(userService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));

        mockMvc.perform(get("/api/users")
                .accept("application/hal+json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._embedded.users").exists())
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/users"));
    }

    @Test
    public void testGetUserByIdWithHateoas() throws Exception {
        UserDto user = new UserDto(1L, "Иван Иванов", "ivan@example.com", 25, LocalDateTime.now());
        when(userService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/api/users/1")
                .accept("application/hal+json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/users/1"))
                .andExpect(jsonPath("$._links.users.href").value("http://localhost/api/users"))
                .andExpect(jsonPath("$._links.update.href").value("http://localhost/api/users/1"))
                .andExpect(jsonPath("$._links.delete.href").value("http://localhost/api/users/1"));
    }

    @Test
    public void testCreateUserWithHateoas() throws Exception {
        UserDto user = new UserDto(1L, "Иван Иванов", "ivan@example.com", 25, LocalDateTime.now());
        when(userService.createUser(any(UserDto.class))).thenReturn(user);

        String userJson = """
                {
                    "name": "Иван Иванов",
                    "email": "ivan@example.com",
                    "age": 25
                }
                """;

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson)
                .accept("application/hal+json"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/users/1"))
                .andExpect(jsonPath("$._links.users.href").value("http://localhost/api/users"));
    }
} 