package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

@Repository
public class ProductRepository {
    private List<Product> productData = new ArrayList<>();

    public Product create(Product product) {
        productData.add(product);
        return product;
    }

    public Product delete(Product product) {
        if (product == null || product.getProductId() == null) return null;
        Iterator<Product> it = productData.iterator();
        while (it.hasNext()) {
            Product p = it.next();
            if (p.getProductId() != null && p.getProductId().equals(product.getProductId())) {
                it.remove();
                return p;
            }
        }
        return null;
    }

    public Product findProductById(UUID productId) {
        if (productId == null) return null;
        for (Product product : productData) {
            if (product.getProductId() != null && product.getProductId().equals(productId)) {
                return product;
            }
        }
        return null;
    }

    public Product update(Product product) {
        if (product == null || product.getProductId() == null) return null;
        for (int i = 0; i < productData.size(); i++) {
            Product p = productData.get(i);
            if (p.getProductId() != null && p.getProductId().equals(product.getProductId())) {
                productData.set(i, product);
                return product;
            }
        }
        return null;
    }

    public Iterator<Product> findAll() {
        return productData.iterator();
    }
}