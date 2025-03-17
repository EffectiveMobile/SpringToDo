package com.emobile.springtodo.repository.contract;

import com.emobile.springtodo.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Интерфейс TodoRepository предоставляет методы для работы с задачами в базе данных.
 *
 * @author Мельников Никита
 */
@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    /**
     * Возвращает список задач с пагинацией.
     *
     * @param limit  максимальное количество задач в результате
     * @param offset смещение для пагинации
     * @return список задач
     */
    @Query(value = "SELECT * FROM todos ORDER BY id LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Todo> findAll(int limit, int offset);
}
