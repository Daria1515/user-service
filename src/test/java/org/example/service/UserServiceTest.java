package org.example.service;

import org.example.dto.UserDto;
import org.example.kafka.UserEventProducer;
import org.example.mapper.UserMapper;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

class UserServiceTest {

    private UserRepository repository;
    private UserEventProducer eventProducer;
    private UserService service;

    @BeforeEach
    void setUp() {
        repository = mock(UserRepository.class);
        eventProducer = mock(UserEventProducer.class);
        service = new UserService(repository, eventProducer);
    }

    @Test
    void testGetAllUsers() {
        when(repository.findAll()).thenReturn(Arrays.asList(
                new User("Igor", "igor@mail.com", 30),
                new User("Katya", "katya@mail.com", 22)
        ));

        assertEquals(2, service.getAllUsers().size());
    }

    @Test
    void testGetUserById() {
        User user = new User("Tanya", "tanya@mail.com", 27);
        user.setId(10L);
        when(repository.findById(10L)).thenReturn(Optional.of(user));

        UserDto dto = service.getUserById(10L);
        assertNotNull(dto);
        assertEquals("Tanya", dto.getName());
    }

    @Test
    void testGetUserById_NotFound() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        UserDto dto = service.getUserById(999L);
        assertThat(dto).isNull();
    }

    @Test
    void testCreateUser() {
        UserDto dto = new UserDto(null, "Nikolay", "kolya@mail.com", 40, null);
        User user = UserMapper.toEntity(dto);
        user.setId(1L);

        when(repository.save(any())).thenReturn(user);

        UserDto created = service.createUser(dto);
        assertNotNull(created);
        assertEquals("Nikolay", created.getName());
        verify(eventProducer).send(any());
    }

    @Test
    void testUpdateUser() {
        User existing = new User("Olya", "olya@mail.com", 32);
        existing.setId(5L);

        when(repository.findById(5L)).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        UserDto updated = service.updateUser(5L, new UserDto(null, "Olga", "olga@mail.com", 33, null));
        assertNotNull(updated);
        assertEquals("Olga", updated.getName());
    }

    @Test
    void testDeleteUser() {
        User user = new User("Test", "test@mail.com", 25);
        user.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(user));

        service.deleteUser(1L);
        verify(repository).deleteById(1L);
        verify(eventProducer).send(any());
    }
}
