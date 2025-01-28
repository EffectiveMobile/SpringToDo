package emobile.by.smertex.springtodo.database.repository.sql.impl;

import emobile.by.smertex.springtodo.database.entity.sql.realisation.Comment;
import emobile.by.smertex.springtodo.database.entity.sql.realisation.Task;
import emobile.by.smertex.springtodo.database.repository.sql.CommentRepository;
import emobile.by.smertex.springtodo.dto.filter.CommentFilter;
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
public class CommentRepositoryImpl implements CommentRepository {

    private SessionFactory sessionFactory;

    @Override
    public Optional<Comment> findById(UUID id) {
        return Optional.of(
                sessionFactory.getCurrentSession().get(Comment.class, id)
        );
    }

    @Override
    public List<Comment> findAllByFilter(UUID taskId, CommentFilter filter, SecurityUserDto user, Pageable pageable) {
        return sessionFactory.getCurrentSession()
                .createNamedQuery("findAllByFilterComment", Comment.class)
                .setParameter("taskId", taskId)
                .setParameter("createdByEmail", filter.createdBy().email())
                .setParameter("createdByRole", filter.createdBy().role())
                .setParameter("securityUserDtoEmail", user.email())
                .setParameter("securityUserDtoIsAdmin", user.isAdmin())
                .setFirstResult(pageable.offset())
                .setMaxResults(pageable.limit())
                .list();
    }

    @Override
    public Comment save(Comment comment) {
        try {
            sessionFactory.getCurrentSession().persist(comment);
            return comment;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Comment update(Comment comment) {
        try {
            return sessionFactory.getCurrentSession().merge(comment);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void deleteAllCommentFromTask(Task task) {
        sessionFactory.getCurrentSession().detach(task);
    }

    @Autowired
    void setSessionFactory(EntityManagerFactory emf){
        sessionFactory = emf.unwrap(SessionFactory.class);
    }
}
