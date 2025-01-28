package emobile.by.smertex.springtodo.database.repository.sql;

import emobile.by.smertex.springtodo.database.entity.sql.realisation.Metainfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MetainfoRepository extends JpaRepository<Metainfo, UUID> {
}