package id.ac.ui.cs.advprog.eshop.service;

public class ProductValidator {
    public static boolean isQuantityInteger(String quantityStr) {
        if (quantityStr == null) return false;
        try {
            Integer.parseInt(quantityStr);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isQuantityPositive(int quantity) {
        return quantity > 0;
    }
}
