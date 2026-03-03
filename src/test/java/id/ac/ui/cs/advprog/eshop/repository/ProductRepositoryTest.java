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
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
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
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(100);

        Product product2 = new Product();
        product2.setProductId("a0f9de46-90b1-437d-a0bf-d0821dde9096");
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
    void testFindByIdNotFound() {
        String unknownId = UUID.randomUUID().toString();
        assertNull(productRepository.findById(unknownId));
    }

    @Test
    void testDeleteNonExistingProduct() {
        String id = UUID.randomUUID().toString();
        productRepository.delete(id);
        Iterator<Product> it = productRepository.findAll();
        assertFalse(it.hasNext());
    }

    @Test
    void testUpdateNonExistingProduct() {
        String id = UUID.randomUUID().toString();
        Product p = new Product();
        p.setProductId(id);
        p.setProductName("NonExisting");
        p.setProductQuantity(1);
        assertNull(productRepository.update(id, p));
    }

    @Test
    void testFindByIdFound() {
        Product p = new Product();
        String id = UUID.randomUUID().toString();
        p.setProductId(id);
        p.setProductName("Exists");
        p.setProductQuantity(10);
        productRepository.create(p);
        Product found = productRepository.findById(id);
        assertNotNull(found);
        assertEquals(id, found.getProductId());
        assertEquals("Exists", found.getProductName());
        assertEquals(10, found.getProductQuantity());
    }

    @Test
    void testDeleteExistingProduct() {
        Product p = new Product();
        String id = UUID.randomUUID().toString();
        p.setProductId(id);
        p.setProductName("ToDelete");
        p.setProductQuantity(5);
        productRepository.create(p);
        productRepository.delete(id);
        assertNull(productRepository.findById(id));
    }

    @Test
    void testUpdateExistingProduct() {
        Product p = new Product();
        String id = UUID.randomUUID().toString();
        p.setProductId(id);
        p.setProductName("Old");
        p.setProductQuantity(1);
        productRepository.create(p);
        Product updated = new Product();
        updated.setProductId(id);
        updated.setProductName("New");
        updated.setProductQuantity(2);
        Product result = productRepository.update(id, updated);
        assertNotNull(result);
        Product after = productRepository.findById(id);
        assertNotNull(after);
        assertEquals("New", after.getProductName());
        assertEquals(2, after.getProductQuantity());
    }

    @Test
    void testUpdateWithNullProductReturnsNull() {
        assertNull(productRepository.update("someId", null));
    }

    @Test
    void testUpdateWithNullIdReturnsNull() {
        Product p = new Product();
        p.setProductId(null);
        p.setProductName("NoId");
        p.setProductQuantity(1);
        assertNull(productRepository.update(null, p));
    }

    @Test
    void testFindByIdWithNullIdReturnsNull() {
        assertNull(productRepository.findById(null));
    }

    @Test
    void testClearRemovesAllProducts() {
        Product p1 = new Product();
        p1.setProductId(UUID.randomUUID().toString());
        p1.setProductName("P1");
        p1.setProductQuantity(1);
        Product p2 = new Product();
        p2.setProductId(UUID.randomUUID().toString());
        p2.setProductName("P2");
        p2.setProductQuantity(2);
        productRepository.create(p1);
        productRepository.create(p2);
        productRepository.clear();
        Iterator<Product> it = productRepository.findAll();
        assertFalse(it.hasNext());
    }

    @Test
    void testStoredProductWithNullIdIsIgnoredByOperations() {
        Product pNull = new Product();
        pNull.setProductId(null);
        pNull.setProductName("NullId");
        pNull.setProductQuantity(5);

        Product pWithId = new Product();
        String id = UUID.randomUUID().toString();
        pWithId.setProductId(id);
        pWithId.setProductName("WithId");
        pWithId.setProductQuantity(10);

        productRepository.create(pNull);
        productRepository.create(pWithId);

        // find by an unknown id should not match the stored null-id product
        assertNull(productRepository.findById(UUID.randomUUID().toString()));

        // update with an unknown id should return null and not affect stored products
        Product toUpdate = new Product();
        String updateId = UUID.randomUUID().toString();
        toUpdate.setProductId(updateId);
        toUpdate.setProductName("Updated");
        toUpdate.setProductQuantity(99);
        assertNull(productRepository.update(updateId, toUpdate));

        // delete with an unknown id should not affect stored products
        productRepository.delete(UUID.randomUUID().toString());

        // ensure both stored products still exist (one with null id and one with id)
        Iterator<Product> it = productRepository.findAll();
        assertTrue(it.hasNext());
        Product first = it.next();
        assertNull(first.getProductId());
        assertTrue(it.hasNext());
        Product second = it.next();
        assertEquals(id, second.getProductId());
        assertFalse(it.hasNext());
    }

    @Test
    void testCreateWithNullProductReturnsNull() {
        assertNull(productRepository.create(null));
    }

    @Test
    void testCreateWithZeroQuantityReturnsNull() {
        Product p = new Product();
        p.setProductId(UUID.randomUUID().toString());
        p.setProductName("ZeroQty");
        p.setProductQuantity(0);
        assertNull(productRepository.create(p));
    }

    @Test
    void testCreateWithNegativeQuantityReturnsNull() {
        Product p = new Product();
        p.setProductId(UUID.randomUUID().toString());
        p.setProductName("NegativeQty");
        p.setProductQuantity(-1);
        assertNull(productRepository.create(p));
    }

    @Test
    void testUpdateWithZeroQuantityReturnsNull() {
        Product p = new Product();
        String id = UUID.randomUUID().toString();
        p.setProductId(id);
        p.setProductName("Original");
        p.setProductQuantity(5);
        productRepository.create(p);

        Product updated = new Product();
        updated.setProductId(id);
        updated.setProductName("Updated");
        updated.setProductQuantity(0);
        assertNull(productRepository.update(id, updated));
    }

    @Test
    void testDeleteWithNullIdDoesNothing() {
        Product p = new Product();
        String id = UUID.randomUUID().toString();
        p.setProductId(id);
        p.setProductName("Test");
        p.setProductQuantity(1);
        productRepository.create(p);

        productRepository.delete(null);

        Iterator<Product> it = productRepository.findAll();
        assertTrue(it.hasNext());
        assertEquals(id, it.next().getProductId());
    }
}
