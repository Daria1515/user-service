package org.example.service;

import org.example.dto.UserDto;
// import org.example.kafka.UserEventProducer;
// import org.example.kafka.UserEvent;
import org.example.mapper.UserMapper;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository repository;
    // private final UserEventProducer userEventProducer;

    public UserService(UserRepository repository) {
        this.repository = repository;
        // this.userEventProducer = userEventProducer;
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
        User savedUser = repository.save(user);

        // userEventProducer.send(new UserEvent(dto.getEmail(), UserEvent.OPERATION_CREATE));
        System.out.println("Событие CREATE отправлено в Kafka для " + dto.getEmail());
        
        return UserMapper.toDto(savedUser);
    }

    public UserDto updateUser(Long id, UserDto dto) {
        User user = repository.findById(id).orElse(null);
        if (user == null) return null;
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setAge(dto.getAge());
        return UserMapper.toDto(repository.save(user));
    }

    public void deleteUser(Long id) {
        User user = repository.findById(id).orElse(null);
        if (user == null) return;
        String email = user.getEmail();
        repository.deleteById(id);

        // userEventProducer.send(new UserEvent(email, UserEvent.OPERATION_DELETE));
        System.out.println("Событие DELETE отправлено в Kafka для " + email);
    }
}
