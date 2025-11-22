package com.solvd.fooddelivery.repository;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, I> {

    boolean create(T entity);

    Optional<T> findById(I id);

    List<T> findAll();

    boolean update(T entity);

    boolean deleteById(I id);
}
