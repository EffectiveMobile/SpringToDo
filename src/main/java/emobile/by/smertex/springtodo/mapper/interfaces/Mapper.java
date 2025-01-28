package emobile.by.smertex.springtodo.mapper.interfaces;

/**
 * Маппер объектов, применяется в рамках проекта для конвертации Entity в DTO. F - from, T - to
 */
public interface Mapper<F, T> {
    T map(F from);

    default T map(F from, T to) {
        return to;
    }
}
