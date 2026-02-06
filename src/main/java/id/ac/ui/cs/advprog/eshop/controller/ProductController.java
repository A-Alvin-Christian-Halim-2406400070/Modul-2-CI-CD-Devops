package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import id.ac.ui.cs.advprog.eshop.service.ProductValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

        int quantity = 0;
        if (ProductValidator.isQuantityInteger(productQuantityStr)) {
            quantity = Integer.parseInt(productQuantityStr);
            if (!ProductValidator.isQuantityPositive(quantity)) {
                errors.put("productQuantity", "Quantity has to be more than 0");
            }
        } else {
            errors.put("productQuantity", "Quantity must be an integer");
        }

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

        int quantity = 0;
        if (ProductValidator.isQuantityInteger(productQuantityStr)) {
            quantity = Integer.parseInt(productQuantityStr);
            if (!ProductValidator.isQuantityPositive(quantity)) {
                errors.put("productQuantity", "Quantity has to be more than 0");
            }
        } else {
            errors.put("productQuantity", "Quantity must be an integer");
        }

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
    public String deleteProduct(@RequestParam("id") String id) {
        System.out.println("[ProductController] deleteProduct called with id=" + id);
        if (id != null && !id.isEmpty()) {
            final UUID uid;
            try {
                uid = UUID.fromString(id);
            } catch (IllegalArgumentException e) {
                return "redirect:/product/list";
            }
            Product deletedProduct = service.findProductById(uid);
            if (deletedProduct != null) {
                service.delete(deletedProduct);
                System.out.println("[ProductController] deleted product id=" + id);
            } else {
                System.out.println("[ProductController] product not found for id=" + id);
            }
        }
        return "redirect:/product/list";
    }

    @GetMapping("/list")
    public String productListPage(Model model) {
        List<Product> allProducts = service.findAll();
        model.addAttribute("products", allProducts);
        return "ProductList";
    }
}
