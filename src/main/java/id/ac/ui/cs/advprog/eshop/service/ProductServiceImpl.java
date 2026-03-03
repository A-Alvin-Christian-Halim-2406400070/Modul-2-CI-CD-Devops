package id.ac.ui.cs.advprog.eshop.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.RepositoryInterface;

@Service
public class ProductServiceImpl implements ProductService{
    private final RepositoryInterface<Product, String> productRepository;

    @Autowired
    public ProductServiceImpl(RepositoryInterface<Product, String> productRepository) {
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
        Product p = productRepository.findById(productId);
        if (p == null) return null;
        productRepository.delete(productId);
        return p;
    }

    @Override
    public Product findById(String productId) {
        if (productId == null) return null;
        return productRepository.findById(productId);
    }

    @Override
    public Product update(String productId, Product product) {
        if (productId == null || product == null) return null;
        product.setProductId(productId);
        return productRepository.update(productId, product);
    }
}