package id.ac.ui.cs.advprog.eshop.service;

public class ProductValidator {
    private ProductValidator() {}
    public static boolean isQuantityInteger(String quantityStr) {
        if (quantityStr == null) return false;
        String s = quantityStr.trim();
        if (s.isEmpty()) return false;
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isQuantityPositive(int quantity) {
        return quantity > 0;
    }
}
