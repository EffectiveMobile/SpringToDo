package com.emobile.springtodo.users.repository.impl;

import com.emobile.springtodo.users.model.User;
import com.emobile.springtodo.users.repository.UserRepositoryDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import java.util.List;
import static com.emobile.springtodo.tasks.repository.impl.HibernateSessionFactory.getSessionFactory;

@Slf4j
@Repository
public class UserRepositoryDaoImpl implements UserRepositoryDao {

    @Override
    public List<User> getAllUsersList(Integer from, Integer size) {

        return (List<User>)  getSessionFactory().openSession().createQuery("From User").list();
    }

    @Override
    public User getUserById(Long userId) {

        return getSessionFactory().openSession().get(User.class, userId);
    }

    @Override
    public User createUserAccount(User newUser) {

        var session = getSessionFactory().openSession();
        var transaction = session.beginTransaction();

        session.persist(newUser);

        transaction.commit();
        session.close();

        return newUser;
    }

    @Override
    public User updateUserAccount(Long userId, User updateUser) {

        var session = getSessionFactory().openSession();
        var transaction = session.beginTransaction();

        session.persist(updateUser);

        transaction.commit();
        session.close();

        return updateUser;
    }

    @Override
    public User deleteUserAccount(Long userId) {

        User user = this.getUserById(userId);

        var session = getSessionFactory().openSession();
        var transaction = session.beginTransaction();

        session.remove(user);

        transaction.commit();
        session.close();

        return user;
    }
}
