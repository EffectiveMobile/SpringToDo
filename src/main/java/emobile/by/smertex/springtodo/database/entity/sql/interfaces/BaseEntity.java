package emobile.by.smertex.springtodo.database.entity.sql.interfaces;

import java.io.Serializable;
import java.util.UUID;

/**
 * Базовая сущность в рамках проекта, необходима для абстрагирования
 */
public interface BaseEntity<T extends Serializable> {
    T getId();

    void setId(UUID id);
}
