package emobile.by.smertex.springtodo.database.repository.realisation;

import emobile.by.smertex.springtodo.database.entity.realisation.Comment;
import emobile.by.smertex.springtodo.database.entity.realisation.Metainfo;
import emobile.by.smertex.springtodo.database.entity.realisation.Task;
import emobile.by.smertex.springtodo.database.entity.realisation.User;
import emobile.by.smertex.springtodo.database.entity.realisation.enums.Priority;
import emobile.by.smertex.springtodo.database.entity.realisation.enums.Role;
import emobile.by.smertex.springtodo.database.entity.realisation.enums.Status;
import emobile.by.smertex.springtodo.database.repository.interfaces.CommentRepository;
import emobile.by.smertex.springtodo.dto.filter.CommentFilter;
import emobile.by.smertex.springtodo.dto.read.Pageable;
import emobile.by.smertex.springtodo.dto.security.SecurityUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {

    private static final String SELECT_SQL = """
                                             SELECT
                                                   c.id             AS c_id,
                                                   c.task_id        AS c_task_id,
                                                   c.content        AS c_content,
                                                   c.created_at     AS c_created_at,
                                                   u1.id            AS u1_id,
                                                   u1.email         AS u1_email,
                                                   u1.password      AS u1_password,
                                                   u1.role          AS u1_role,
                                                   u2.id            AS u2_id,
                                                   u2.email         AS u2_email,
                                                   u2.password      AS u2_password,
                                                   u2.role          AS u2_role,
                                                   t.id             AS t_id,
                                                   t.name           AS t_name,
                                                   t.status         AS t_status,
                                                   t.priority       AS t_priority,
                                                   t.description    AS t_description,
                                                   t.metainfo_id    AS t_metainfo_id,
                                                   t.performer_id   AS t_performer_id,
                                                   m.id             AS m_id,
                                                   m.created_at     AS m_created_at,
                                                   m.updated_at     AS m_updated_at,
                                                   m.created_by     AS m_created_by,
                                                   u3.id            AS u3_id,
                                                   u3.email         AS u3_email,
                                                   u3.password      AS u3_password,
                                                   u3.role          AS u3_role
                                             """;

    private static final String FIND_BY_ID_SQL = SELECT_SQL + """
                                                 FROM comment c
                                                 JOIN users u1      ON      c.created_by = u1.id
                                                 JOIN task t        ON      c.task_id = t.id
                                                 JOIN users u2      ON      t.performer_id = u2.id
                                                 JOIN metainfo m    ON      t.metainfo_id = m.id
                                                 JOIN users u3      ON      m.created_by = u3.id
                                                 WHERE c.id = ?
                                                 """;
    private static final String FIND_BY_FILTER_SQL = SELECT_SQL + """
                                                 FROM comment c
                                                 JOIN users u1      ON      c.created_by = u1.id
                                                 JOIN task t        ON      c.task_id = t.id
                                                 JOIN users u2      ON      t.performer_id = u2.id
                                                 JOIN metainfo m    ON      t.metainfo_id = m.id
                                                 JOIN users u3      ON      m.created_by = u3.id
                                                 WHERE t.id = ?
                                                    AND (?::VARCHAR IS NULL OR ? = u1.email)
                                                    AND (?::VARCHAR IS NULL OR ? = u1.role)
                                                    AND (?::VARCHAR = u2.email OR ? = TRUE)
                                                 LIMIT ?
                                                 OFFSET ?
                                                     """;

    private static final String SAVE_SQL = """
                                           INSERT INTO comment(task_id, content, created_at, created_by)
                                           VALUES (?, ?, ?, ?)
                                           """;

    private static final String UPDATE_SQL = """
                                             UPDATE comment
                                             SET content = ?, created_at = ?, created_by = ?
                                             WHERE id = ?
                                             """;

    private static final String DELETE_ALL_COMMENT_FROM_TASK = """
                                                               DELETE 
                                                               FROM comment
                                                               WHERE task_id = ?
                                                               """;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Comment> findById(UUID id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    FIND_BY_ID_SQL,
                    (resultSet, rowNum) -> buildEntity(resultSet),
                    id
            ));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Comment> findAllByFilter(UUID taskId, CommentFilter filter, SecurityUserDto user, Pageable pageable) {
        String createdByRole = filter.createdBy().role() == null
                ? null
                : filter.createdBy().role().name();
        return jdbcTemplate.query(
                FIND_BY_FILTER_SQL,
                (resultSet, rowNum) -> buildEntity(resultSet),
                taskId, filter.createdBy().email(), filter.createdBy().email(), createdByRole, createdByRole, user.email(), user.isAdmin(), pageable.limit(), pageable.offset());
    }

    @Override
    public Comment save(Comment comment) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, new String[]{"id"});
            preparedStatement.setObject(1, comment.getTask().getId());
            preparedStatement.setString(2, comment.getContent());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(comment.getCreatedAt()));
            preparedStatement.setObject(4, comment.getCreatedBy().getId());

            return preparedStatement;
        }, keyHolder);

        UUID id = (UUID) keyHolder.getKeys().get("id");
        if(id != null)
            comment.setId(id);
        return comment;
    }

    @Override
    public Comment update(Comment comment) {
        jdbcTemplate.update(
                UPDATE_SQL,
                comment.getContent(), Timestamp.valueOf(comment.getCreatedAt()), comment.getCreatedBy().getId(), comment.getId()
        );
        return comment;
    }

    public void deleteAllCommentFromTask(Task task) {
        jdbcTemplate.update(
                DELETE_ALL_COMMENT_FROM_TASK,
                task.getId());
    }

    private Comment buildEntity(ResultSet resultSet) throws SQLException {
        User createdByComment = new User();
        createdByComment.setId(UUID.fromString(resultSet.getString("u1_id")));
        createdByComment.setEmail(resultSet.getString("u1_email"));
        createdByComment.setPassword(resultSet.getString("u1_password"));
        createdByComment.setRole(Role.valueOf(resultSet.getString("u1_role")));

        User taskPerformer = new User();
        taskPerformer.setId(UUID.fromString(resultSet.getString("u2_id")));
        taskPerformer.setEmail(resultSet.getString("u2_email"));
        taskPerformer.setPassword(resultSet.getString("u2_password"));
        taskPerformer.setRole(Role.valueOf(resultSet.getString("u2_role")));

        User createdByTask = new User();
        createdByTask.setId(UUID.fromString(resultSet.getString("u3_id")));
        createdByTask.setEmail(resultSet.getString("u3_email"));
        createdByTask.setPassword(resultSet.getString("u3_password"));
        createdByTask.setRole(Role.valueOf(resultSet.getString("u3_role")));

        Metainfo metainfo = new Metainfo();
        metainfo.setId(UUID.fromString(resultSet.getString("m_id")));
        metainfo.setCreatedAt(resultSet.getTimestamp("m_created_at").toLocalDateTime());
        metainfo.setUpdatedAt(resultSet.getTimestamp("m_updated_at").toLocalDateTime());
        metainfo.setCreatedBy(createdByTask);

        Task task = new Task();
        task.setId(UUID.fromString(resultSet.getString("t_id")));
        task.setName(resultSet.getString("t_name"));
        task.setStatus(Status.valueOf(resultSet.getString("t_status")));
        task.setPriority(Priority.valueOf(resultSet.getString("t_priority")));
        task.setDescription(resultSet.getString("t_description"));
        task.setMetainfo(metainfo);
        task.setPerformer(createdByTask);

        Comment comment = new Comment();
        comment.setId(UUID.fromString(resultSet.getString("c_id")));
        comment.setTask(task);
        comment.setContent(resultSet.getString("c_content"));
        comment.setCreatedBy(createdByComment);
        comment.setCreatedAt(resultSet.getTimestamp("c_created_at").toLocalDateTime());
        return comment;
    }
}
