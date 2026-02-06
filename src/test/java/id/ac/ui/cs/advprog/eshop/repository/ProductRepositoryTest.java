package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {

    @InjectMocks
    ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository = new ProductRepository();
    }

    @Test
    void testCreateAndFind(){
        Product product = new Product();
        product.setProductId(UUID.fromString("eb558e9f-1c39-460e-8860-71af6af63bd6"));
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);

        productRepository.create(product);
        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product.getProductId(), savedProduct.getProductId());
        assertEquals(product.getProductName(), savedProduct.getProductName());
        assertEquals(product.getProductQuantity(), savedProduct.getProductQuantity());
    }

    @Test
    void testFindAllIfEmpty(){
        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testFindAllIfMoreThanOneProduct(){
        Product product1 = new Product();
        product1.setProductId(UUID.fromString("eb558e9f-1c39-460e-8860-71af6af63bd6"));
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(100);

        Product product2 = new Product();
        product2.setProductId(UUID.fromString("a0f9de46-90b1-437d-a0bf-d0821dde9096"));
        product2.setProductName("Sampo Cap Usep");
        product2.setProductQuantity(50);

        productRepository.create(product1);
        productRepository.create(product2);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product1.getProductId(), savedProduct.getProductId());
        assertEquals(product1.getProductName(), savedProduct.getProductName());
        assertEquals(product1.getProductQuantity(), savedProduct.getProductQuantity());
        savedProduct = productIterator.next();
        assertEquals(product2.getProductId(), savedProduct.getProductId());
        assertEquals(product2.getProductName(), savedProduct.getProductName());
        assertEquals(product2.getProductQuantity(), savedProduct.getProductQuantity());
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testFindProductByIdNotFound() {
        UUID unknownId = UUID.randomUUID();
        assertNull(productRepository.findProductById(unknownId));
    }

    @Test
    void testDeleteNonExistingProduct() {
        Product p = new Product();
        p.setProductId(UUID.randomUUID());
        Product deleted = productRepository.delete(p);
        assertNull(deleted);
        Iterator<Product> it = productRepository.findAll();
        assertFalse(it.hasNext());
    }

    @Test
    void testUpdateNonExistingProduct() {
        Product p = new Product();
        p.setProductId(UUID.randomUUID());
        p.setProductName("NonExisting");
        p.setProductQuantity(1);
        assertNull(productRepository.update(p));
    }

    @Test
    void testFindProductByIdFound() {
        Product p = new Product();
        UUID id = UUID.randomUUID();
        p.setProductId(id);
        p.setProductName("Exists");
        p.setProductQuantity(10);
        productRepository.create(p);
        Product found = productRepository.findProductById(id);
        assertNotNull(found);
        assertEquals(id, found.getProductId());
        assertEquals("Exists", found.getProductName());
        assertEquals(10, found.getProductQuantity());
    }

    @Test
    void testDeleteExistingProduct() {
        Product p = new Product();
        UUID id = UUID.randomUUID();
        p.setProductId(id);
        p.setProductName("ToDelete");
        p.setProductQuantity(5);
        productRepository.create(p);
        Product deleted = productRepository.delete(p);
        assertNotNull(deleted);
        assertEquals(id, deleted.getProductId());
        assertNull(productRepository.findProductById(id));
    }

    @Test
    void testUpdateExistingProduct() {
        Product p = new Product();
        UUID id = UUID.randomUUID();
        p.setProductId(id);
        p.setProductName("Old");
        p.setProductQuantity(1);
        productRepository.create(p);
        Product updated = new Product();
        updated.setProductId(id);
        updated.setProductName("New");
        updated.setProductQuantity(2);
        Product result = productRepository.update(updated);
        assertNotNull(result);
        Product after = productRepository.findProductById(id);
        assertNotNull(after);
        assertEquals("New", after.getProductName());
        assertEquals(2, after.getProductQuantity());
    }

}
