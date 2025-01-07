package emobile.by.smertex.springtodo.database.repository.sql.realisation;

import emobile.by.smertex.springtodo.database.entity.sql.realisation.Metainfo;
import emobile.by.smertex.springtodo.database.entity.sql.realisation.Task;
import emobile.by.smertex.springtodo.database.entity.sql.realisation.User;
import emobile.by.smertex.springtodo.database.entity.sql.realisation.enums.Priority;
import emobile.by.smertex.springtodo.database.entity.sql.realisation.enums.Role;
import emobile.by.smertex.springtodo.database.entity.sql.realisation.enums.Status;
import emobile.by.smertex.springtodo.database.repository.sql.interfaces.CommentRepository;
import emobile.by.smertex.springtodo.database.repository.sql.interfaces.TaskRepository;
import emobile.by.smertex.springtodo.dto.filter.TaskFilter;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepository {

    private static final String SELECT_SQL = """
                                             SELECT
                                                u1.id            AS u1_id,
                                                u1.email         AS u1_email,
                                                u1.password      AS u1_password,
                                                u1.role          AS u1_role,
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
                                                u2.id            AS u2_id,
                                                u2.email         AS u2_email,
                                                u2.password      AS u2_password,
                                                u2.role          AS u2_role
                                             """;

    private static final String FIND_BY_ID = SELECT_SQL + """
                                             FROM task t
                                             JOIN users u1      ON      t.performer_id = u1.id
                                             JOIN metainfo m    ON      t.metainfo_id = m.id
                                             JOIN users u2      ON      m.created_by = u2.id
                                             WHERE t.id = ?
                                             """;

    private static final String FIND_BY_FILTER_SQL = SELECT_SQL +  """
                                                     FROM task t
                                                     JOIN users u1      ON      t.performer_id = u1.id
                                                     JOIN metainfo m    ON      t.metainfo_id = m.id
                                                     JOIN users u2      ON      m.created_by = u2.id
                                                     WHERE  (?::VARCHAR IS NULL OR ? = u2.email)
                                                        AND (?::VARCHAR IS NULL OR ? = u2.role)
                                                        AND (?::VARCHAR IS NULL OR ? < m.created_at)
                                                        AND (?::VARCHAR IS NULL OR ? < m.updated_at)
                                                        AND (?::VARCHAR IS NULL OR ? = u1.email)
                                                        AND (?::VARCHAR IS NULL OR ? = u1.role)
                                                        AND (?::VARCHAR IS NULL OR ? = t.status)
                                                        AND (?::VARCHAR IS NULL OR ? = t.priority)
                                                        AND (?::VARCHAR IS NULL OR t.name LIKE ? || '%')
                                                        AND (?::VARCHAR = u1.email OR ? = TRUE)
                                                     LIMIT ?
                                                     OFFSET ?
                                                     """;

    private static final String SAVE_SQL = """
                                           INSERT INTO task(name, status, priority, description, metainfo_id, performer_id)
                                           VALUES (?, ?, ?, ?, ?, ?)
                                           """;

    private static final String UPDATE_SQL = """
                                             UPDATE task
                                             SET name = ?, 
                                                 status = ?, 
                                                 priority = ?, 
                                                 description = ?, 
                                                 metainfo_id = ?, 
                                                 performer_id = ?
                                             WHERE id = ?
                                             """;

    private static final String DELETE_SQL = "DELETE FROM task WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;

    private final CommentRepository commentRepository;

    @Override
    public Optional<Task> findById(UUID id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    FIND_BY_ID,
                    (resultSet, rowNum) -> buildEntity(resultSet),
                    id
            ));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Task> findAllByFilter(TaskFilter filter, SecurityUserDto user, Pageable pageable) {
        String performerRole = filter.performer().role() == null
                ? null
                : filter.performer().role().name();
        String createdByRole = filter.createdBy().role() == null
                ? null
                : filter.createdBy().role().name();
        String status = filter.status() == null
                ? null
                : filter.status().name();
        String priority = filter.priority() == null
                ? null
                : filter.priority().name();

        return jdbcTemplate.query(
                FIND_BY_FILTER_SQL,
                (resultSet, rowNum) -> buildEntity(resultSet),
                filter.createdBy().email(), filter.createdBy().email(),
                createdByRole, createdByRole,
                filter.createdAt(), filter.createdAt(),
                filter.updatedAt(), filter.updatedAt(),
                filter.performer().email(), filter.performer().email(),
                performerRole, performerRole,
                status, status,
                priority, priority,
                filter.name(), filter.name(),
                user.email(), user.isAdmin(),
                pageable.limit(), pageable.offset()
                );
    }

    @Override
    public Task save(Task task) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                (connection) -> {
                    PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, new String[]{"id"});

                    preparedStatement.setString(1, task.getName());
                    preparedStatement.setString(2, task.getStatus().name());
                    preparedStatement.setString(3, task.getPriority().name());
                    preparedStatement.setString(4, task.getDescription());
                    preparedStatement.setObject(5, task.getMetainfo().getId());
                    preparedStatement.setObject(6, task.getPerformer().getId());

                    return preparedStatement;
                }, keyHolder);

        UUID id = (UUID) keyHolder.getKeys().get("id");
        if(id != null)
            task.setId(id);
        return task;
    }

    @Override
    public Task update(Task task) {
        jdbcTemplate.update(
                UPDATE_SQL,
                task.getName(), task.getStatus().name(), task.getPriority().name(), task.getDescription(), task.getMetainfo().getId(), task.getPerformer().getId(), task.getId()
        );
        return task;
    }

    @Override
    public void delete(Task task) {
        commentRepository.deleteAllCommentFromTask(task);
        jdbcTemplate.update(DELETE_SQL, task.getId());
    }

    private Task buildEntity(ResultSet resultSet) throws SQLException {
        User taskPerformer = new User();
        taskPerformer.setId(UUID.fromString(resultSet.getString("u1_id")));
        taskPerformer.setEmail(resultSet.getString("u1_email"));
        taskPerformer.setPassword(resultSet.getString("u1_password"));
        taskPerformer.setRole(Role.valueOf(resultSet.getString("u1_role")));

        User createdByTask = new User();
        createdByTask.setId(UUID.fromString(resultSet.getString("u2_id")));
        createdByTask.setEmail(resultSet.getString("u2_email"));
        createdByTask.setPassword(resultSet.getString("u2_password"));
        createdByTask.setRole(Role.valueOf(resultSet.getString("u2_role")));

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
        task.setPerformer(taskPerformer);

        return task;
    }
}
