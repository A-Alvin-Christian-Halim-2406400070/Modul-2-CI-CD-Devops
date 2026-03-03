package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.CarServiceImpl;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import id.ac.ui.cs.advprog.eshop.service.ProductValidator;

import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
            try{ product.setProductId(UUID.fromString(productId)); } catch (IllegalArgumentException e) {}
            product.setProductName(productName);
            model.addAttribute("product", product);
            model.addAttribute("productQuantityRaw", productQuantityRaw);
            model.addAttribute("errors", errors);
            return "EditProduct";
        }
        Product product = new Product();
        try{ product.setProductId(UUID.fromString(productId)); } catch (IllegalArgumentException e) { return "redirect:list"; }
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
        try {
            UUID.fromString(productId);
        } catch (IllegalArgumentException e) {
            return "redirect:list";
        }
        service.deleteProductById(productId);
        return "redirect:list";
    }
}

@Controller
@RequestMapping("/car")
class CarController extends ProductController {

    @Autowired
    private CarServiceImpl carservice;

    @GetMapping("/createCar")
    public String createCarPage(Model model) {
        Car car = new Car();
        model.addAttribute("car", car);
        return "CreateCar";
    }

    @PostMapping("/createCar")
    public String createCarPost(@ModelAttribute Car car, Model model){
        carservice.create(car);
        return "redirect:listCar";
    }

    @GetMapping("/listCar")
    public String carListPage(Model model){
        List<Car> allCars = carservice.findAll();
        model.addAttribute("cars", allCars);
        return "CarList";
    }

    @GetMapping("/editCar/{carId}")
    public String editCarPage(@PathVariable String carId, Model model) {
        Car car = carservice.findById(carId);
        model.addAttribute("car", car);
        return "EditCar";
    }

    @PostMapping("/editCar")
    public String editCarPost(@ModelAttribute Car car, Model model) {
        carservice.update(car.getCarId(), car);
        return "redirect:listCar";
    }

    @PostMapping("/deleteCar")
    public String deleteCar(@RequestParam("carId") String carId) {
        carservice.deleteCarById(carId);
        return "redirect:listCar";
    }
}