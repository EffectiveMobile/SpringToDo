package emobile.by.smertex.springtodo.database.repository.sql;

import emobile.by.smertex.springtodo.database.entity.sql.realisation.Comment;
import emobile.by.smertex.springtodo.database.entity.sql.realisation.Task;
import emobile.by.smertex.springtodo.dto.filter.CommentFilter;
import emobile.by.smertex.springtodo.dto.read.Pageable;
import emobile.by.smertex.springtodo.dto.security.SecurityUserDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommentRepository {

    Optional<Comment> findById(UUID id);

    List<Comment> findAllByFilter(UUID taskId, CommentFilter filter, SecurityUserDto user, Pageable pageable);

    Comment save(Comment comment);

    Comment update(Comment comment);

    void deleteAllCommentFromTask(Task task);
}