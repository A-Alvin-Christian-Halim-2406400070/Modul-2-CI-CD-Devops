package id.ac.ui.cs.advprog.eshop.repository;

public interface RepositoryInterface<T, ID> extends ReadRepository<T, ID>, WriteRepository<T, ID> {
    // Combines ReadRepository and WriteRepository
}
