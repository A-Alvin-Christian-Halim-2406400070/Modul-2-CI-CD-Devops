package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import java.util.List;
import java.util.UUID;

public interface ProductService {
    Product create(Product product);
    List<Product> findAll();
    Product findById(String productId);
    Product update(UUID productId, Product product);
    Product deleteProductById(String productId);
}