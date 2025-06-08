package org.example.service;

import org.example.dao.UserDao;
import org.example.model.User;

import java.util.List;

public class UserService {
    private final UserDao dao;

    public UserService(UserDao dao) {
        this.dao = dao;
    }

    public void registerUser(String name, String email, int age) {
        dao.save(new User(name, email, age));
    }

    public List<User> listUsers() {
        return dao.getAll();
    }

    public User getUser(Long id){
        return dao.get(id);
    }

    public void updateUser(User user){
        dao.update(user);
    }

    public void deleteUser(Long id){
        dao.delete(id);
    }
}
