package org.example;

import org.example.dao.UserDao;
import org.example.model.User;
import org.example.util.HibernateUtil;
import org.hibernate.SessionFactory;

import java.util.Scanner;

public class Main {
    //private static final UserDao dao = new UserDao();

    public static void main(String[] args) {

        SessionFactory factory = HibernateUtil.getSessionFactory();
        UserDao dao = new UserDao(factory);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("""
                1. Создать пользователя
                2. Показать всех пользователей
                3. Обновить пользователя
                4. Удалить пользователя
                5. Выход
            """);

            switch (scanner.nextLine()) {
                case "1" -> {
                    System.out.print("Имя: ");
                    String name = scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Возраст: ");
                    int age = Integer.parseInt(scanner.nextLine());
                    dao.save(new User(name, email, age));
                }
                case "2" -> dao.getAll().forEach(u -> System.out.println(u.getId() + ": " + u.getName()));
                case "3" -> {
                    System.out.print("ID: ");
                    Long id = Long.parseLong(scanner.nextLine());
                    User user = dao.get(id);
                    if (user != null) {
                        System.out.print("Новое имя: ");
                        user.setName(scanner.nextLine());
                        dao.update(user);
                    } else System.out.println("Пользователь не найден");
                }
                case "4" -> {
                    System.out.print("ID: ");
                    dao.delete(Long.parseLong(scanner.nextLine()));
                }
                case "5" -> System.exit(0);
            }
        }
    }
}