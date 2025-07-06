package org.example.service;

import org.example.dto.UserDto;
import org.example.kafka.UserEvent;
import org.example.kafka.UserEventProducer;
import org.example.kafka.UserOperation;
import org.example.mapper.UserMapper;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceKafkaTest {

    private UserRepository repository;
    private UserEventProducer eventProducer;
    private UserService service;
    private ArgumentCaptor<UserEvent> eventCaptor;

    @BeforeEach
    void setUp() {
        repository = mock(UserRepository.class);
        eventProducer = mock(UserEventProducer.class);
        service = new UserService(repository, eventProducer);
        eventCaptor = ArgumentCaptor.forClass(UserEvent.class);
    }

    @Test
    void testCreateUserSendsCreateEvent() {
        UserDto dto = new UserDto(null, "Test User", "test@example.com", 25, null);
        User user = UserMapper.toEntity(dto);
        user.setId(1L);
        when(repository.save(any())).thenReturn(user);

        service.createUser(dto);

        verify(eventProducer).send(eventCaptor.capture());
        UserEvent capturedEvent = eventCaptor.getValue();
        
        assertEquals("test@example.com", capturedEvent.getEmail());
        assertEquals(UserOperation.CREATE, capturedEvent.getOperation());
    }

    @Test
    void testUpdateUserSendsUpdateEvent() {
        User existingUser = new User("Old Name", "test@example.com", 25);
        existingUser.setId(1L);
        UserDto updateDto = new UserDto(null, "New Name", "test@example.com", 26, null);
        
        when(repository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(repository.save(any())).thenReturn(existingUser);

        service.updateUser(1L, updateDto);

        verify(eventProducer).send(eventCaptor.capture());
        UserEvent capturedEvent = eventCaptor.getValue();
        
        assertEquals("test@example.com", capturedEvent.getEmail());
        assertEquals(UserOperation.UPDATE, capturedEvent.getOperation());
    }

    @Test
    void testDeleteUserSendsDeleteEvent() {
        User user = new User("Test User", "test@example.com", 25);
        user.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(user));

        service.deleteUser(1L);

        verify(eventProducer).send(eventCaptor.capture());
        UserEvent capturedEvent = eventCaptor.getValue();
        
        assertEquals("test@example.com", capturedEvent.getEmail());
        assertEquals(UserOperation.DELETE, capturedEvent.getOperation());
    }

    @Test
    void testUpdateUserNotFoundDoesNotSendEvent() {
        when(repository.findById(999L)).thenReturn(Optional.empty());
        UserDto updateDto = new UserDto(null, "New Name", "test@example.com", 26, null);

        service.updateUser(999L, updateDto);

        verify(eventProducer, never()).send(any());
    }

    @Test
    void testDeleteUserNotFoundDoesNotSendEvent() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        service.deleteUser(999L);

        verify(eventProducer, never()).send(any());
    }
} 