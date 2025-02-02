package com.emobile.springtodo.repositories;

public interface JdbcRepository<T, R> {

    R save(R entity);

    R update(R newEntity);

    R findById(T id);

    void deleteById(T id);

}
