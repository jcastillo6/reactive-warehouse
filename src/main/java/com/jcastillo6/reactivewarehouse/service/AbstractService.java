package com.jcastillo.warehouse.service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface AbstractService<T> {
    Set<T> findAll();
    long count();
    Optional<T> findById(UUID id);
    T save(T entity);
    void delete(T entity);
}
