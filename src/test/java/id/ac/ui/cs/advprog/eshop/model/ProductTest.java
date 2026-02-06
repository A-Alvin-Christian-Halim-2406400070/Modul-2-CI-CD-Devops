package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;
import java.util.UUID;
class ProductTest {
    Product product;
    @BeforeEach
    void setUp() {
        this.product = new Product();
        this.product.setProductId(UUID.fromString("eb558e9f-1c39-460e-8860-71af6af63bd6"));
        this.product.setProductName("Sampo Cap Bambang");
        this.product.setProductQuantity(100);
    }

    @Test
    void testGetProductId() {
        assertEquals(UUID.fromString("eb558e9f-1c39-460e-8860-71af6af63bd6"), product.getProductId());
    }

    @Test
    void testGetProductIdNegative() {
        assertNotEquals(UUID.fromString("eb558e9f-1c39-460e-8860-71af6af63bd7"), product.getProductId());
    }

    @Test
    void testGetProductName() {
        assertEquals("Sampo Cap Bambang", product.getProductName());
    }

    @Test
    void testGetProductNameNegative() {
        assertNotEquals("Sampo Cap Usep", product.getProductName());
    }

    @Test
    void testGetProductQuantity() {
        assertEquals(100, product.getProductQuantity());
    }

    @Test
    void testGetProductQuantityNegative() {
        assertNotEquals(10, product.getProductQuantity());
    }
}

