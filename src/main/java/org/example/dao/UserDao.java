package org.example.dao;

import org.example.model.User;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);
    private final SessionFactory sessionFactory;

    public UserDao() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    //конструктор для тестов
    public UserDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(User user) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(user);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            logger.error("Ошибка при сохранении пользователя: {}", user, e);
        }
    }

    public User get(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(User.class, id);
        } catch (Exception e) {
            logger.error("Ошибка при получении пользователя по id = {}", id, e);
            return null;
        }
    }

    public List<User> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from User", User.class).list();
        } catch (Exception e) {
            logger.error("Ошибка при получении всех пользователей", e);
            return List.of();
        }
    }

    public void update(User user) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(user);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            logger.error("Ошибка при обновлении пользователя: {}", user, e);
        }
    }

    public void delete(Long id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            logger.error("Ошибка при удалении пользователя с id = {}", id, e);
        }
    }
}
