package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService service;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    private ProductController controller;

    @BeforeEach
    void setUp() {
        controller = new ProductController(service);
    }

    @Test
    void homePageReturnsIndexView() {
        String view = controller.homePage(model);

        assertEquals("index", view);
        verifyNoInteractions(service);
    }

    @Test
    void createProductPageAddsEmptyProductToModel() {
        String view = controller.createProductPage(model);

        assertEquals("CreateProduct", view);
        verify(model).addAttribute(eq("product"), any(Product.class));
        verifyNoInteractions(service);
    }

    @Test
    void createProductWithNonIntegerQuantityReturnsCreateProductWithErrors() {
        String view = controller.createProduct("Phone", "abc", model);

        assertEquals("CreateProduct", view);

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(model).addAttribute(eq("product"), productCaptor.capture());
        verify(model).addAttribute("productQuantityRaw", "abc");

        @SuppressWarnings("unchecked")
        ArgumentCaptor<Map<String, String>> errorsCaptor = ArgumentCaptor.forClass(Map.class);
        verify(model).addAttribute(eq("errors"), errorsCaptor.capture());

        assertEquals("Phone", productCaptor.getValue().getProductName());
        assertEquals("Quantity must be an integer", errorsCaptor.getValue().get("productQuantity"));
        verify(service, never()).create(any(Product.class));
    }

    @Test
    void createProductWithNonPositiveQuantityReturnsCreateProductWithErrors() {
        String view = controller.createProduct("Phone", "0", model);

        assertEquals("CreateProduct", view);

        @SuppressWarnings("unchecked")
        ArgumentCaptor<Map<String, String>> errorsCaptor = ArgumentCaptor.forClass(Map.class);
        verify(model).addAttribute(eq("errors"), errorsCaptor.capture());
        assertEquals("Quantity has to be more than 0", errorsCaptor.getValue().get("productQuantity"));

        verify(service, never()).create(any(Product.class));
    }

    @Test
    void createProductWithValidInputsPersistsProductAndRedirects() {
        String view = controller.createProduct("Phone", "7", model);

        assertEquals("redirect:/product/list", view);

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(service).create(productCaptor.capture());
        assertEquals("Phone", productCaptor.getValue().getProductName());
        assertEquals(7, productCaptor.getValue().getProductQuantity());
    }

    @Test
    void editProductPageWithInvalidIdRedirects() {
        String view = controller.editProductPage("not-a-uuid", model);

        assertEquals("redirect:/product/list", view);
        verifyNoInteractions(service);
    }

    @Test
    void editProductPageWithMissingProductRedirects() {
        UUID id = UUID.randomUUID();
        when(service.findProductById(id)).thenReturn(null);

        String view = controller.editProductPage(id.toString(), model);

        assertEquals("redirect:/product/list", view);
        verify(service).findProductById(id);
        verify(model, never()).addAttribute(eq("product"), any());
    }

    @Test
    void editProductPageWithValidProductReturnsEditProduct() {
        UUID id = UUID.randomUUID();
        Product existing = new Product();
        existing.setProductId(id);
        when(service.findProductById(id)).thenReturn(existing);

        String view = controller.editProductPage(id.toString(), model);

        assertEquals("EditProduct", view);
        verify(service).findProductById(id);
        verify(model).addAttribute("product", existing);
    }

    @Test
    void editProductWithInvalidProductIdRedirects() {
        String view = controller.editProduct("not-a-uuid", "Laptop", "10", model);

        assertEquals("redirect:/product/list", view);
        verifyNoInteractions(service);
    }

    @Test
    void editProductWithMissingExistingProductRedirects() {
        UUID id = UUID.randomUUID();
        when(service.findProductById(id)).thenReturn(null);

        String view = controller.editProduct(id.toString(), "Laptop", "10", model);

        assertEquals("redirect:/product/list", view);
        verify(service).findProductById(id);
        verify(service, never()).update(any(Product.class));
    }

    @Test
    void editProductWithInvalidQuantityReturnsEditProductWithErrors() {
        UUID id = UUID.randomUUID();
        Product existing = new Product();
        existing.setProductId(id);
        when(service.findProductById(id)).thenReturn(existing);

        String view = controller.editProduct(id.toString(), "Laptop", "nope", model);

        assertEquals("EditProduct", view);
        verify(service).findProductById(id);
        verify(service, never()).update(any(Product.class));

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(model).addAttribute(eq("product"), productCaptor.capture());
        verify(model).addAttribute("productQuantityRaw", "nope");

        @SuppressWarnings("unchecked")
        ArgumentCaptor<Map<String, String>> errorsCaptor = ArgumentCaptor.forClass(Map.class);
        verify(model).addAttribute(eq("errors"), errorsCaptor.capture());

        assertEquals(id, productCaptor.getValue().getProductId());
        assertEquals("Laptop", productCaptor.getValue().getProductName());
        assertEquals("Quantity must be an integer", errorsCaptor.getValue().get("productQuantity"));
    }

    @Test
    void editProductWithInvalidQuantityAndSecondUuidParseFailureUsesRandomFallbackId() {
        UUID id = UUID.randomUUID();
        UUID fallbackId = UUID.randomUUID();
        Product existing = new Product();
        existing.setProductId(id);
        when(service.findProductById(id)).thenReturn(existing);

        try (MockedStatic<UUID> uuidMock = mockStatic(UUID.class, CALLS_REAL_METHODS)) {
            uuidMock.when(() -> UUID.fromString(id.toString()))
                    .thenReturn(id)
                    .thenThrow(new IllegalArgumentException("forced parse failure"));
            uuidMock.when(UUID::randomUUID).thenReturn(fallbackId);

            String view = controller.editProduct(id.toString(), "Laptop", "invalid", model);

            assertEquals("EditProduct", view);
            verify(service).findProductById(id);
            verify(service, never()).update(any(Product.class));

            ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
            verify(model).addAttribute(eq("product"), productCaptor.capture());
            assertEquals(fallbackId, productCaptor.getValue().getProductId());
            assertEquals("Laptop", productCaptor.getValue().getProductName());
        }
    }

    @Test
    void editProductWithValidInputsUpdatesProductAndRedirects() {
        UUID id = UUID.randomUUID();
        Product existing = new Product();
        existing.setProductId(id);
        existing.setProductName("Old");
        existing.setProductQuantity(1);
        when(service.findProductById(id)).thenReturn(existing);

        String view = controller.editProduct(id.toString(), "Laptop", "10", model);

        assertEquals("redirect:/product/list", view);
        verify(service).findProductById(id);
        verify(service).update(existing);
        assertEquals("Laptop", existing.getProductName());
        assertEquals(10, existing.getProductQuantity());
    }

    @Test
    void deleteProductWithNullIdRedirectsWithErrorMessage() {
        String view = controller.deleteProduct(null, redirectAttributes);

        assertEquals("redirect:/product/list", view);
        verify(redirectAttributes).addFlashAttribute("errorMessage", "Product doesn't exists");
        verifyNoInteractions(service);
    }

    @Test
    void deleteProductWithEmptyIdRedirectsWithErrorMessage() {
        String view = controller.deleteProduct("", redirectAttributes);

        assertEquals("redirect:/product/list", view);
        verify(redirectAttributes).addFlashAttribute("errorMessage", "Product doesn't exists");
        verifyNoInteractions(service);
    }

    @Test
    void deleteProductWithInvalidUuidRedirectsWithErrorMessage() {
        String view = controller.deleteProduct("invalid", redirectAttributes);

        assertEquals("redirect:/product/list", view);
        verify(redirectAttributes).addFlashAttribute("errorMessage", "Product doesn't exists");
        verifyNoInteractions(service);
    }

    @Test
    void deleteProductWithMissingProductRedirectsWithErrorMessage() {
        UUID id = UUID.randomUUID();
        when(service.findProductById(id)).thenReturn(null);

        String view = controller.deleteProduct(id.toString(), redirectAttributes);

        assertEquals("redirect:/product/list", view);
        verify(service).findProductById(id);
        verify(service, never()).delete(any(Product.class));
        verify(redirectAttributes).addFlashAttribute("errorMessage", "Product doesn't exists");
    }

    @Test
    void deleteProductWithExistingProductDeletesAndRedirects() {
        UUID id = UUID.randomUUID();
        Product product = new Product();
        product.setProductId(id);
        when(service.findProductById(id)).thenReturn(product);

        String view = controller.deleteProduct(id.toString(), redirectAttributes);

        assertEquals("redirect:/product/list", view);
        verify(service).findProductById(id);
        verify(service).delete(product);
        verify(redirectAttributes, never()).addFlashAttribute(eq("errorMessage"), anyString());
    }

    @Test
    void productListPageAddsProductsAndReturnsProductListView() {
        List<Product> products = List.of(new Product(), new Product());
        when(service.findAll()).thenReturn(products);

        String view = controller.productListPage(model);

        assertEquals("ProductList", view);
        verify(service).findAll();
        verify(model).addAttribute("products", products);
    }
}
