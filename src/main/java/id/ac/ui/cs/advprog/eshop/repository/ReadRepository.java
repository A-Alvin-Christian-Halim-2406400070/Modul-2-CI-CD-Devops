package id.ac.ui.cs.advprog.eshop.repository;

import java.util.Iterator;

public interface ReadRepository<T, ID> {
    T findById(ID id);
    Iterator<T> findAll();
}
