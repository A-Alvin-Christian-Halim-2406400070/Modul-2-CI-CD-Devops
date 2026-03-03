package id.ac.ui.cs.advprog.eshop.model;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Product {
    private String productId = UUID.randomUUID().toString();
    private String productName;
    private int productQuantity;
}