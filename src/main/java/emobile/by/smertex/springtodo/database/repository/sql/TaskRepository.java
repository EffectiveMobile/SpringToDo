package emobile.by.smertex.springtodo.database.repository.sql;

import emobile.by.smertex.springtodo.database.entity.sql.realisation.Task;
import emobile.by.smertex.springtodo.dto.filter.TaskFilter;
import emobile.by.smertex.springtodo.dto.security.SecurityUserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    /**
     * Метод динамической фильтрации, который совмещен с пагинацией
     */
    @Query("""
            select t from Task t
            join fetch t.performer u1
            join Metainfo m on t.metainfo.id = m.id
            join User u on t.performer.id = u.id
            where (:#{#filter.createdBy.email} is null or :#{#filter.createdBy.email} = m.createdBy.email)
            and (:#{#filter.createdBy.role} is null or :#{#filter.createdBy.role} = m.createdBy.role)
            and (cast(cast(:#{#filter.createdAt} as text) as timestamp) is null or m.createdAt >= :#{#filter.createdAt})
            and (cast(cast(:#{#filter.updatedAt} as text) as timestamp) is null or m.updatedAt <= :#{#filter.updatedAt})
            and (:#{#filter.status} is null or :#{#filter.status} = t.status)
            and (:#{#filter.priority} is null or :#{#filter.priority} = t.priority)
            and (:#{#filter.performer.email} is null or :#{#filter.performer.email} = t.performer.email)
            and (:#{#filter.performer.role} is null or :#{#filter.performer.role} = t.performer.role)
            and (:#{#filter.name} is null or t.name like %:#{#filter.name}%)
            and (:#{#user.email} = t.performer.email or :#{#user.isAdmin} = true)
            """)
    Page<Task> findAllByFilter(TaskFilter filter, SecurityUserDto user, Pageable pageable);
}
