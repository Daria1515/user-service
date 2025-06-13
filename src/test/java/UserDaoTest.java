

import org.example.dao.UserDao;
import org.example.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import static org.mockito.Mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserDaoTest {

    private static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:14.1-alpine")
                    .withDatabaseName("postgres")
                    .withUsername("postgres")
                    .withPassword("postgres");

    private SessionFactory sessionFactory;
    private UserDao userDao;

    @BeforeAll
    void startContainer() {
        postgres.start();

        Configuration configuration = new Configuration();

        configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        configuration.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        configuration.setProperty("hibernate.connection.username", postgres.getUsername());
        configuration.setProperty("hibernate.connection.password", postgres.getPassword());
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        configuration.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        configuration.setProperty("hibernate.show_sql", "true");

        configuration.addAnnotatedClass(User.class);

        sessionFactory = configuration.buildSessionFactory();
        userDao = new UserDao(sessionFactory);
    }

    @BeforeEach
    void clearDatabase() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.createQuery("delete from User").executeUpdate(); // очистка таблицы
            tx.commit();
        }
    }

    @AfterAll
    void stopContainer() {
        if (sessionFactory != null) sessionFactory.close();
        postgres.stop();
    }

    @Test
    void testSaveAndGetUser() {
        User user = new User("Алиса", "alice@example.com", 25);
        userDao.save(user);

        User fetched = userDao.get(user.getId());

        assertNotNull(fetched);
        assertEquals("Алиса", fetched.getName());
        assertEquals("alice@example.com", fetched.getEmail());
        assertEquals(25, fetched.getAge());
    }

    @Test
    void testGetAllUsers() {
        userDao.save(new User("Боб", "bob@example.com", 30));
        userDao.save(new User("Чарли", "charlie@example.com", 35));

        List<User> users = userDao.getAll();

        assertEquals(2, users.size());
    }

    @Test
    void testUpdateUser() {
        User user = new User("Давид", "david@example.com", 40);
        userDao.save(user);

        user.setName("Давид обновился");
        userDao.update(user);

        User updated = userDao.get(user.getId());
        assertEquals("Давид обновился", updated.getName());
    }

    @Test
    void testDeleteUser() {
        User user = new User("Ева", "eva@example.com", 45);
        userDao.save(user);

        userDao.delete(user.getId());

        User deleted = userDao.get(user.getId());
        assertNull(deleted);
    }

    @Test
    void testSave_ExceptionHandled() {
        SessionFactory mockFactory = mock(SessionFactory.class);
        when(mockFactory.openSession()).thenThrow(new RuntimeException("Ошибка сессии"));

        UserDao faultyDao = new UserDao(mockFactory);
        User testUser = new User("Ошибка", "fail@example.com", 99);

        assertDoesNotThrow(() -> faultyDao.save(testUser));
    }

    @Test
    void testUpdate_ExceptionHandled() {
        SessionFactory mockFactory = mock(SessionFactory.class);
        when(mockFactory.openSession()).thenThrow(new RuntimeException("Ошибка при обновлении"));

        UserDao faultyDao = new UserDao(mockFactory);
        User testUser = new User("Ошибка", "update@example.com", 77);

        assertDoesNotThrow(() -> faultyDao.update(testUser));
    }

    @Test
    void testDelete_ExceptionHandled() {
        SessionFactory mockFactory = mock(SessionFactory.class);
        when(mockFactory.openSession()).thenThrow(new RuntimeException("Ошибка при удалении"));

        UserDao faultyDao = new UserDao(mockFactory);

        assertDoesNotThrow(() -> faultyDao.delete(123L));
    }

    @Test
    void testGetAll_ExceptionHandled() {
        SessionFactory mockFactory = mock(SessionFactory.class);
        when(mockFactory.openSession()).thenThrow(new RuntimeException("Ошибка при получении всех"));

        UserDao faultyDao = new UserDao(mockFactory);
        List<User> result = faultyDao.getAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

}
