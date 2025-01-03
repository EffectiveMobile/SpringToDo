package emobile.by.smertex.springtodo.database.repository.realisation;

import emobile.by.smertex.springtodo.database.entity.realisation.Comment;
import emobile.by.smertex.springtodo.database.repository.interfaces.CommentRepository;
import emobile.by.smertex.springtodo.dto.filter.CommentFilter;
import emobile.by.smertex.springtodo.dto.read.Pageable;
import emobile.by.smertex.springtodo.dto.security.SecurityUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Comment> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public List<Comment> findAllByFilter(UUID taskId, CommentFilter filter, SecurityUserDto user, Pageable pageable) {
        return List.of();
    }

    @Override
    public Comment save(Comment comment) {
        return null;
    }

    @Override
    public Comment update(Comment comment) {
        return null;
    }
}
