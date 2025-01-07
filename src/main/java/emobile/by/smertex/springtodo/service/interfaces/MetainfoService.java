package emobile.by.smertex.springtodo.service.interfaces;

import emobile.by.smertex.springtodo.database.entity.sql.realisation.Metainfo;

import java.util.Optional;

/**
 * Сервис для работы с метаинформацией. Обеспечивает ее создание и сохранения в рамках БД
 */
public interface MetainfoService {
    Optional<Metainfo> save();
}
