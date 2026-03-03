package id.ac.ui.cs.advprog.eshop.repository;

import java.util.Iterator;

public interface RepositoryInterface<T, ID> {
    T create(T entity);
    Iterator<T> findAll();
    T findById(ID id);
    T update(ID id, T entity);
    void delete(ID id);
}
