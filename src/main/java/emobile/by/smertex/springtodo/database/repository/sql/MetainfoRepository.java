package emobile.by.smertex.springtodo.database.repository.sql;

import emobile.by.smertex.springtodo.database.entity.sql.realisation.Metainfo;

public interface MetainfoRepository {
    Metainfo save(Metainfo metainfo);

    Metainfo update(Metainfo metainfo);
}