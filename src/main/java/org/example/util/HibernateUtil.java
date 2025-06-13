package org.example.util;

import org.example.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.util.Properties;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildDefaultSessionFactory();

    private static SessionFactory buildDefaultSessionFactory() {
        return buildSessionFactory(
                "jdbc:postgresql://localhost:5432/testdb",
                "postgres",
                "postgres18"
        );
    }

    public static SessionFactory buildSessionFactory(String url, String username, String password) {
        try {
            Configuration configuration = new Configuration();

            Properties settings = new Properties();
            settings.put("hibernate.connection.driver_class", "org.postgresql.Driver");
            settings.put("hibernate.connection.url", url);
            settings.put("hibernate.connection.username", username);
            settings.put("hibernate.connection.password", password);
            settings.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            settings.put("hibernate.hbm2ddl.auto", "update");
            settings.put("hibernate.show_sql", "true");
            settings.put("hibernate.format_sql", "true");

            configuration.setProperties(settings);


            configuration.addAnnotatedClass(User.class);

            return configuration.buildSessionFactory(
                    new StandardServiceRegistryBuilder()
                            .applySettings(configuration.getProperties())
                            .build()
            );
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError("Ошибка создания SessionFactory: " + ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
