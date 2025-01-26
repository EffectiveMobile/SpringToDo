package com.emobile.springtodo.tasks.repository.impl;

import com.emobile.springtodo.tasks.model.Task;
import com.emobile.springtodo.tasks.repository.TaskRepositoryDao;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import static com.emobile.springtodo.tasks.repository.impl.HibernateSessionFactory.getSessionFactory;

@Slf4j
@Repository
public class TaskRepositoryDaoImpl implements TaskRepositoryDao {

    @Override
    public List<Task> getListOfAllTasks(Integer from, Integer size) {

        return (List<Task>) getSessionFactory().openSession()
                .createQuery("From Task")
                .list();
    }

    @Override
    public Task getTaskByAuthorIdAndTaskId(Long authorId, Long taskId) {

        var session = getSessionFactory().openSession();
        var transaction = session.beginTransaction();

        Query<Task> query = session.createQuery("from Task where author_id = :authorId");
        Task result = query.uniqueResult();
        transaction.commit();
        session.close();

        return result;
    }

    @Override
    public Task createTaskByAuthorId(Task task) {

        var session = getSessionFactory().openSession();
        var transaction = session.beginTransaction();

        session.persist(task);

        transaction.commit();
        session.close();

        return task;
    }

    @Override
    public Task update(Task updateTask) {

        var session = getSessionFactory().openSession();
        var transaction = session.beginTransaction();

        session.persist(updateTask);

        transaction.commit();
        session.close();

        return updateTask;
    }

    @Override
    public Task deleteTaskByAuthorId(Long authorId, Long taskId) {

        Task task = this.getTaskByAuthorIdAndTaskId(authorId, taskId);

        var session = getSessionFactory().openSession();
        var transaction = session.beginTransaction();

        session.remove(task);

        transaction.commit();
        session.close();

        return task;
    }
}
