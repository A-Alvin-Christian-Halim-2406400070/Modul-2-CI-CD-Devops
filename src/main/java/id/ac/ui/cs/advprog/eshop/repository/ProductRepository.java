package id.ac.ui.cs.advprog.eshop.repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Repository;

import id.ac.ui.cs.advprog.eshop.model.Product;

@Repository
public class ProductRepository implements RepositoryInterface<Product, String> {
    private List<Product> productData = new ArrayList<>();

    @Override
    public Product create(Product product) {
        if (product == null) return null;
        if (product.getProductQuantity() <= 0) return null;
        productData.add(product);
        return product;
    }

    @Override
    public void delete(String id) {
        if (id == null) return;
        productData.removeIf(p -> p.getProductId() != null && p.getProductId().equals(id));
    }

    @Override
    public Product findById(String productId) {
        if (productId == null) return null;
        for (Product product : productData) {
            if (product.getProductId() != null && product.getProductId().equals(productId)) {
                return product;
            }
        }
        return null;
    }

    @Override
    public Product update(String id, Product product) {
        if (product == null || id == null) return null;
        if (product.getProductQuantity() <= 0) return null;
        for (int i = 0; i < productData.size(); i++) {
            Product p = productData.get(i);
            if (p.getProductId() != null && p.getProductId().equals(id)) {
                product.setProductId(id);
                productData.set(i, product);
                return product;
            }
        }
        return null;
    }

    @Override
    public Iterator<Product> findAll() {
        return productData.iterator();
    }

    public void clear() {
        productData.clear();
    }
}