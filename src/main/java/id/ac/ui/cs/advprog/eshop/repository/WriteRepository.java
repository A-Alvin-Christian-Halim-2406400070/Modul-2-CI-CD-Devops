package id.ac.ui.cs.advprog.eshop.repository;

public interface WriteRepository<T, ID> {
    T create(T entity);
    T update(ID id, T entity);
    void delete(ID id);
}
