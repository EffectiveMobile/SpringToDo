package com.emobile.springtodo.tasks.repository.impl;

import com.emobile.springtodo.tasks.model.Task;
import com.emobile.springtodo.users.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Component;

@Component
public class HibernateSessionFactory {

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        try {
            if (sessionFactory == null) {

                Configuration configuration = new Configuration().configure("hibernate.cfg.xml");

                configuration.addAnnotatedClass(User.class);
                configuration.addAnnotatedClass(Task.class);

                StandardServiceRegistryBuilder builder =
                        new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());

                sessionFactory = configuration.buildSessionFactory(builder.build());
            }

        } catch (Exception e) {
            System.out.println("Исключение!" + e);
        }

        return sessionFactory;
    }
}
