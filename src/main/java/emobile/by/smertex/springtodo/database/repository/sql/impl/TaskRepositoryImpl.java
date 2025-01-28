package emobile.by.smertex.springtodo.database.repository.sql.impl;

import emobile.by.smertex.springtodo.database.entity.sql.realisation.Task;
import emobile.by.smertex.springtodo.database.repository.sql.TaskRepository;
import emobile.by.smertex.springtodo.dto.filter.TaskFilter;
import emobile.by.smertex.springtodo.dto.read.Pageable;
import emobile.by.smertex.springtodo.dto.security.SecurityUserDto;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TaskRepositoryImpl implements TaskRepository {

    private SessionFactory sessionFactory;

    @Override
    public Optional<Task> findById(UUID id) {
        return Optional.of(
                sessionFactory.getCurrentSession().get(Task.class, id)
        );
    }

    @Override
    public List<Task> findAllByFilter(TaskFilter filter, SecurityUserDto user, Pageable pageable) {
        return sessionFactory.getCurrentSession()
                .createNamedQuery("findAllByFilterTask", Task.class)
                .setParameter("filterCreatedByEmail", filter.createdBy().email())
                .setParameter("filterCreatedByRole", filter.createdBy().role())
                .setParameter("filterCreatedAt", filter.createdAt())
                .setParameter("filterUpdatedAt", filter.updatedAt())
                .setParameter("status", filter.status())
                .setParameter("priority", filter.priority())
                .setParameter("filterPerformerEmail", filter.performer().email())
                .setParameter("filterPerformerRole", filter.performer().role())
                .setParameter("filterName", filter.name())
                .setParameter("userEmail", user.email())
                .setParameter("userIsAdmin", user.isAdmin())
                .setFirstResult(pageable.offset())
                .setMaxResults(pageable.limit())
                .list();
    }

    @Override
    public Task save(Task task) {
        try {
            sessionFactory.getCurrentSession().persist(task);
            return task;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Task update(Task task) {
        try {
            return sessionFactory.getCurrentSession().merge(task);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void delete(Task task) {
        sessionFactory.getCurrentSession().detach(task);
    }

    @Autowired
    void setSessionFactory(EntityManagerFactory emf){
        sessionFactory = emf.unwrap(SessionFactory.class);
    }
}
