package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product create(Product product) {
        if (product == null) return null;
        if (product.getProductQuantity() <= 0) return null;
        productRepository.create(product);
        return product;
    }

    @Override
    public List<Product> findAll() {
        Iterator<Product> productIterator = productRepository.findAll();
        List<Product> allProduct = new ArrayList<>();
        productIterator.forEachRemaining(allProduct::add);
        return allProduct;
    }

    @Override
    public Product deleteProductById(String productId) {
        if (productId == null) return null;
        try {
            UUID id = UUID.fromString(productId);
            Product p = productRepository.findProductById(id);
            if (p == null) return null;
            return productRepository.delete(p);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public Product findById(String productId) {
        if (productId == null) return null;
        try {
            UUID id = UUID.fromString(productId);
            return productRepository.findProductById(id);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public Product update(UUID productId, Product product) {
        if (productId == null || product == null) return null;
        product.setProductId(productId);
        return productRepository.update(product);
    }

    @Override
    public Product delete(Product product) {
        return productRepository.delete(product);
    }

    @Override
    public Product findProductById(UUID productId) {
        if (productId == null) return null;
        return productRepository.findProductById(productId);
    }

    @Override
    public Product update(Product product) {
        return productRepository.update(product);
    }
}