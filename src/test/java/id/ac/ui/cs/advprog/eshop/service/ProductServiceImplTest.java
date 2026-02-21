package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductServiceImpl productService;

    Product sample;

    @BeforeEach
    void setUp() {
        sample = new Product();
        sample.setProductId(UUID.randomUUID());
        sample.setProductName("Sample");
        sample.setProductQuantity(1);
    }

    @Test
    void testCreateForwardsToRepositoryAndReturnsProduct() {
        when(productRepository.create(sample)).thenReturn(sample);
        Product result = productService.create(sample);
        assertEquals(sample, result);
        verify(productRepository).create(sample);
    }

    @Test
    void testDeleteForwardsToRepositoryAndReturnsProduct() {
        when(productRepository.delete(sample)).thenReturn(sample);
        Product result = productService.delete(sample);
        assertEquals(sample, result);
        verify(productRepository).delete(sample);
    }

    @Test
    void testFindAllConvertsIteratorToList() {
        List<Product> products = new ArrayList<>();
        Product p2 = new Product();
        p2.setProductId(UUID.randomUUID());
        p2.setProductName("P2");
        p2.setProductQuantity(2);
        products.add(sample);
        products.add(p2);
        Iterator<Product> it = products.iterator();
        when(productRepository.findAll()).thenReturn(it);
        List<Product> result = productService.findAll();
        assertEquals(2, result.size());
        assertEquals(sample.getProductId(), result.get(0).getProductId());
        assertEquals(p2.getProductId(), result.get(1).getProductId());
    }

    @Test
    void testFindAllWhenEmpty() {
        List<Product> products = new ArrayList<>();
        Iterator<Product> it = products.iterator();
        when(productRepository.findAll()).thenReturn(it);
        List<Product> result = productService.findAll();
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindProductByIdWithNullReturnsNull() {
        assertNull(productService.findProductById(null));
        verifyNoInteractions(productRepository);
    }

    @Test
    void testFindProductByIdForwardsToRepository() {
        when(productRepository.findProductById(sample.getProductId())).thenReturn(sample);
        Product result = productService.findProductById(sample.getProductId());
        assertEquals(sample, result);
        verify(productRepository).findProductById(sample.getProductId());
    }

    @Test
    void testUpdateForwardsToRepository() {
        when(productRepository.update(sample)).thenReturn(sample);
        Product result = productService.update(sample);
        assertEquals(sample, result);
        verify(productRepository).update(sample);
    }
}
