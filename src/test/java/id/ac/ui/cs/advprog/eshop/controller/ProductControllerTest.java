package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Test
    void testConstructor() {
        ProductService svc = mock(ProductService.class);
        ProductController ctrl = new ProductController(svc);
        Model model = mock(Model.class);
        when(svc.findAll()).thenReturn(java.util.List.of());
        String view = ctrl.productListPage(model);
        assertEquals("ProductList", view);
        verify(svc).findAll();
    }
}
