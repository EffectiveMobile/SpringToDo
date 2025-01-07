package emobile.by.smertex.springtodo.database.repository.sql.realisation;

import emobile.by.smertex.springtodo.database.entity.sql.realisation.Metainfo;
import emobile.by.smertex.springtodo.database.repository.sql.interfaces.MetainfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class MetainfoRepositoryImpl implements MetainfoRepository {

    private static final String SAVE_SQL = """
                                           INSERT INTO metainfo (created_at, updated_at, created_by)
                                           VALUES (?, ?, ?)
                                           """;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Metainfo save(Metainfo metainfo) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, new String[] {"id"});
            preparedStatement.setTimestamp(1, Timestamp.valueOf(metainfo.getCreatedAt()));
            preparedStatement.setTimestamp(2, Timestamp.valueOf(metainfo.getUpdatedAt()));
            preparedStatement.setObject(3, metainfo.getCreatedBy().getId());
            return preparedStatement;
        }, keyHolder);

        UUID id = (UUID) keyHolder.getKeys().get("id");
        if(id != null)
            metainfo.setId(id);
        return metainfo;
    }
}