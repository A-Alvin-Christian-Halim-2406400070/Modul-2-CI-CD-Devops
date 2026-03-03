package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import java.util.List;
import java.util.UUID;

public interface ProductService {
    public Product create(Product product);
    public List<Product> findAll();
    public Product deleteProductById(String productId);
    public Product findById(String productId);
    public Product update(UUID productId, Product product);
    Product delete(Product product);
    Product findProductById(UUID productId);
    Product update(Product product);
}