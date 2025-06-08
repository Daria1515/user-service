import org.example.dao.UserDao;
import org.example.model.User;
import org.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser() {
        userService.registerUser("Алиса", "alice@example.com", 25);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userDao).save(captor.capture());

        User savedUser = captor.getValue();
        assertEquals("Алиса", savedUser.getName());
        assertEquals("alice@example.com", savedUser.getEmail());
        assertEquals(25, savedUser.getAge());
    }

    @Test
    void testListUsers() {
        List<User> mockUsers = Arrays.asList(
                new User("Боб", "bob@example.com", 30),
                new User("Чарли", "charlie@example.com", 35)
        );
        when(userDao.getAll()).thenReturn(mockUsers);

        List<User> result = userService.listUsers();

        assertEquals(2, result.size());
        assertEquals("Боб", result.get(0).getName());
        assertEquals("Чарли", result.get(1).getName());
        verify(userDao).getAll();
    }

    @Test
    void testGetUser() {
        User mockUser = new User("Давид", "david@example.com", 40);
        mockUser.setId(1L);
        when(userDao.get(1L)).thenReturn(mockUser);

        User result = userService.getUser(1L);

        assertEquals("Давид", result.getName());
        verify(userDao).get(1L);
    }

    @Test
    void testUpdateUser() {
        User user = new User("Ева", "eva@example.com", 45);
        user.setId(2L);

        userService.updateUser(user);

        verify(userDao).update(user);
    }

    @Test
    void testDeleteUser() {
        userService.deleteUser(3L);

        verify(userDao).delete(3L);
    }
}
