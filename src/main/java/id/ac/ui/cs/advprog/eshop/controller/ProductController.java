package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public String createProduct(@ModelAttribute Product product) {
        service.create(product);
        return "redirect:/product/list";
    }

    // Accept id as a form parameter to avoid relying on path variable rendering
    @PostMapping("/delete")
    public String deleteProduct(@RequestParam("id") String id) {
        System.out.println("[ProductController] deleteProduct called with id=" + id);
        if (id != null && !id.isEmpty()) {
            Product deletedProduct = service.findById(id);
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
