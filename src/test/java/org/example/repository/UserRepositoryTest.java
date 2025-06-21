package org.example.repository;

import org.example.UserServiceApplication;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@DataJpaTest
//@Testcontainers
class UserRepositoryTest {

//    @Container
//    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
//            .withDatabaseName("test-db")
//            .withUsername("test")
//            .withPassword("test");
//
//    @DynamicPropertySource
//    static void overrideProps(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", postgres::getJdbcUrl);
//        registry.add("spring.datasource.username", postgres::getUsername);
//        registry.add("spring.datasource.password", postgres::getPassword);
//    }

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Сохранение и поиск пользователя")
    void saveAndFindById() {
        User user = new User("Анна", "anna@example.com", 28);
        userRepository.save(user);

        Optional<User> found = userRepository.findById(user.getId());
        assertTrue(found.isPresent());
        assertEquals("Анна", found.get().getName());
    }

    @Test
    @DisplayName("Проверка existsById и deleteById")
    void existsAndDelete() {
        User user = new User("Миша", "misha@example.com", 35);
        userRepository.save(user);
        Long id = user.getId();

        assertTrue(userRepository.existsById(id));
        userRepository.deleteById(id);
        assertFalse(userRepository.existsById(id));
    }

    @Test
    @DisplayName("Поиск всех пользователей")
    void findAllUsers() {
        userRepository.save(new User("Петя", "pety@example.com", 20));
        userRepository.save(new User("Лена", "lena@example.com", 24));

        assertEquals(2, userRepository.findAll().size());
    }
}
