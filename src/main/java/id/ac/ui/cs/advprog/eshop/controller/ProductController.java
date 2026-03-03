package id.ac.ui.cs.advprog.eshop.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import id.ac.ui.cs.advprog.eshop.service.ProductValidator;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping("/create")
    public String createProductPage(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "CreateProduct";
    }

    @PostMapping("/create")
    public String createProductPost(@RequestParam("productName") String productName, @RequestParam("productQuantity") String productQuantityRaw, Model model){
        Map<String, String> errors = new HashMap<>();
        if (!ProductValidator.isQuantityInteger(productQuantityRaw)){
            errors.put("productQuantity", "Quantity must be an integer");
        } else {
            int q = Integer.parseInt(productQuantityRaw.trim());
            if (!ProductValidator.isQuantityPositive(q)){
                errors.put("productQuantity", "Quantity must be positive");
            }
        }
        if (!errors.isEmpty()){
            Product product = new Product();
            product.setProductName(productName);
            model.addAttribute("product", product);
            model.addAttribute("productQuantityRaw", productQuantityRaw);
            model.addAttribute("errors", errors);
            return "CreateProduct";
        }
        Product product = new Product();
        product.setProductName(productName);
        product.setProductQuantity(Integer.parseInt(productQuantityRaw.trim()));
        Product created = service.create(product);
        if (created == null) {
            errors.put("productQuantity", "Invalid quantity");
            model.addAttribute("product", product);
            model.addAttribute("productQuantityRaw", productQuantityRaw);
            model.addAttribute("errors", errors);
            return "CreateProduct";
        }
        return "redirect:list";
    }

    @GetMapping("/list")
    public String productListPage(Model model){
        List<Product> allProducts = service.findAll();
        model.addAttribute("products", allProducts);
        return "ProductList";
    }

    @GetMapping("/edit/{productId}")
    public String editProductPage(@PathVariable String productId, Model model) {
        Product product = service.findById(productId);
        model.addAttribute("product", product);
        return "EditProduct";
    }

    @PostMapping("/edit")
    public String editProductPost(@RequestParam("productId") String productId, @RequestParam("productName") String productName, @RequestParam("productQuantity") String productQuantityRaw, Model model) {
        Map<String, String> errors = new HashMap<>();
        if (!ProductValidator.isQuantityInteger(productQuantityRaw)){
            errors.put("productQuantity", "Quantity must be an integer");
        } else {
            int q = Integer.parseInt(productQuantityRaw.trim());
            if (!ProductValidator.isQuantityPositive(q)){
                errors.put("productQuantity", "Quantity must be positive");
            }
        }
        if (!errors.isEmpty()){
            Product product = new Product();
            product.setProductId(productId);
            product.setProductName(productName);
            model.addAttribute("product", product);
            model.addAttribute("productQuantityRaw", productQuantityRaw);
            model.addAttribute("errors", errors);
            return "EditProduct";
        }
        Product product = new Product();
        product.setProductId(productId);
        product.setProductName(productName);
        product.setProductQuantity(Integer.parseInt(productQuantityRaw.trim()));
        Product updated = service.update(product.getProductId(), product);
        if (updated == null) {
            errors.put("productQuantity", "Invalid quantity");
            model.addAttribute("product", product);
            model.addAttribute("productQuantityRaw", productQuantityRaw);
            model.addAttribute("errors", errors);
            return "EditProduct";
        }
        return "redirect:list";
    }

    @PostMapping("/delete")
    public String deleteProduct(@RequestParam("productId") String productId) {
        if (productId == null || productId.trim().isEmpty()) return "redirect:list";
        service.deleteProductById(productId);
        return "redirect:list";
    }
}