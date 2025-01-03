package emobile.by.smertex.springtodo.database.repository.realisation;

import emobile.by.smertex.springtodo.database.entity.realisation.Metainfo;
import emobile.by.smertex.springtodo.database.repository.interfaces.MetainfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MetainfoRepositoryImpl implements MetainfoRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Metainfo save(Metainfo metainfo) {
        return null;
    }
}
