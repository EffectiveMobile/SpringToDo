package emobile.by.smertex.springtodo.database.repository.sql;

import emobile.by.smertex.springtodo.database.entity.sql.realisation.Comment;
import emobile.by.smertex.springtodo.dto.filter.CommentFilter;
import emobile.by.smertex.springtodo.dto.security.SecurityUserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {
    /**
     * Метод динамической фильтрации, который совмещен с пагинацией
     */
    @Query("""
           select c from Comment c
           join fetch c.createdBy u1
           join Task t on t.id = c.task.id
           join User u2 on u2.id = t.performer.id
           where t.id = :taskId
           and (:#{#commentFilter.createdBy.email} is null or :#{#commentFilter.createdBy.email} = c.createdBy.email) 
           and (:#{#commentFilter.createdBy.role} is null or :#{#commentFilter.createdBy.role} = c.createdBy.role)
           and (:#{#securityUserDto.email} = t.performer.email or :#{#securityUserDto.isAdmin} = true)
           """)
    Page<Comment> findAllByFilter(UUID taskId, CommentFilter commentFilter, SecurityUserDto securityUserDto, Pageable pageable);
}