package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import id.ac.ui.cs.advprog.eshop.service.ProductValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping({"/product", ""})
public class ProductController {
    @Autowired
    private ProductService service;

    @GetMapping("/")
    public String homePage(Model model) {
        return "index";
    }

    @GetMapping("/create")
    public String createProductPage(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "CreateProduct";
    }

    @PostMapping("/create")
    public String createProduct(@RequestParam("productName") String productName,
                                @RequestParam("productQuantity") String productQuantityStr,
                                Model model) {
        Map<String, String> errors = new HashMap<>();

        Integer quantity = parseAndValidateQuantity(productQuantityStr, errors);

        if (!errors.isEmpty()) {
            Product product = new Product();
            product.setProductName(productName);
            model.addAttribute("product", product);
            model.addAttribute("productQuantityRaw", productQuantityStr);
            model.addAttribute("errors", errors);
            return "CreateProduct";
        }

        Product product = new Product();
        product.setProductName(productName);
        product.setProductQuantity(quantity);
        service.create(product);
        return "redirect:/product/list";
    }

    @GetMapping("/edit")
    public String editProductPage(@RequestParam("id") String id, Model model) {
        final UUID uid;
        try {
            uid = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            return "redirect:/product/list";
        }
        Product product = service.findProductById(uid);
        if (product == null) {
            return "redirect:/product/list";
        }
        model.addAttribute("product", product);
        return "EditProduct";
    }

    @PostMapping("/edit")
    public String editProduct(@RequestParam("productId") String productId,
                              @RequestParam("productName") String productName,
                              @RequestParam("productQuantity") String productQuantityStr,
                              Model model) {
        Map<String, String> errors = new HashMap<>();

        Integer quantity = parseAndValidateQuantity(productQuantityStr, errors);

        final UUID uid;
        try {
            uid = UUID.fromString(productId);
        } catch (IllegalArgumentException e) {
            return "redirect:/product/list";
        }
        Product existing = service.findProductById(uid);
        if (existing == null) {
            return "redirect:/product/list";
        }

        if (!errors.isEmpty()) {
            Product product = new Product();
            try {
                product.setProductId(UUID.fromString(productId));
            } catch (IllegalArgumentException e) {
                product.setProductId(UUID.randomUUID());
            }
            product.setProductName(productName);
            model.addAttribute("product", product);
            model.addAttribute("productQuantityRaw", productQuantityStr);
            model.addAttribute("errors", errors);
            return "EditProduct";
        }

        existing.setProductName(productName);
        existing.setProductQuantity(quantity);
        service.update(existing);
        return "redirect:/product/list";
    }

    @PostMapping("/delete")
    public String deleteProduct(@RequestParam("id") String id, RedirectAttributes redirectAttributes) {
        if (id == null || id.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Product doesn't exists");
            return "redirect:/product/list";
        }
        final UUID uid;
        try {
            uid = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Product doesn't exists");
            return "redirect:/product/list";
        }
        Product deletedProduct = service.findProductById(uid);
        if (deletedProduct != null) {
            service.delete(deletedProduct);
            return "redirect:/product/list";
        }
        redirectAttributes.addFlashAttribute("errorMessage", "Product doesn't exists");
        return "redirect:/product/list";
    }

    @GetMapping("/list")
    public String productListPage(Model model) {
        List<Product> allProducts = service.findAll();
        model.addAttribute("products", allProducts);
        return "ProductList";
    }

    private Integer parseAndValidateQuantity(String productQuantityStr, Map<String, String> errors) {
        if (!ProductValidator.isQuantityInteger(productQuantityStr)) {
            errors.put("productQuantity", "Quantity must be an integer");
            return null;
        }
        int quantity;
        try {
            quantity = Integer.parseInt(productQuantityStr);
        } catch (NumberFormatException e) {
            errors.put("productQuantity", "Quantity must be an integer");
            return null;
        }
        if (!ProductValidator.isQuantityPositive(quantity)) {
            errors.put("productQuantity", "Quantity has to be more than 0");
            return null;
        }
        return quantity;
    }
}
