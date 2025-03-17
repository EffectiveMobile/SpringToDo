package com.emobile.springtodo.repository.impl;

import com.emobile.springtodo.model.Todo;
import com.emobile.springtodo.repository.contract.TodoRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TodoRepositoryImpl implements TodoRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public TodoRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Todo> findAll(int limit, int offset) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        List<Todo> todos = session.createQuery("FROM Todo ORDER BY id", Todo.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .list();

        tx.commit();
        session.close();

        return todos;
    }

    @Override
    public Todo findById(Long id) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        Todo todo = session.get(Todo.class, id);

        tx.commit();
        session.close();

        return todo;
    }

    @Override
    public Todo save(Todo todo) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        session.save(todo);

        tx.commit();
        session.close();

        return todo;
    }

    @Override
    public Todo update(Todo todo) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        session.update(todo);

        tx.commit();
        session.close();

        return todo;
    }

    @Override
    public void delete(Long id) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        session.createQuery("DELETE FROM Todo WHERE id = :todo_id").setParameter("todo_id", id).executeUpdate();

        tx.commit();
        session.close();
    }
}
