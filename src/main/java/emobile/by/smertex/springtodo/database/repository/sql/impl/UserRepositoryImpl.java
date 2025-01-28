package emobile.by.smertex.springtodo.database.repository.sql.impl;

import emobile.by.smertex.springtodo.database.entity.sql.realisation.User;
import emobile.by.smertex.springtodo.database.repository.sql.UserRepository;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private SessionFactory sessionFactory;

    @Override
    public Optional<User> findByEmail(String email) {
        try {
            return Optional.of(
                    sessionFactory.getCurrentSession()
                            .createNamedQuery("findByEmailUser", User.class)
                            .setParameter("email", email)
                            .getSingleResult()
            );
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.of(
                sessionFactory.getCurrentSession().get(User.class, id)
        );
    }

    @Autowired
    void setSessionFactory(EntityManagerFactory emf){
        sessionFactory = emf.unwrap(SessionFactory.class);
    }
}
