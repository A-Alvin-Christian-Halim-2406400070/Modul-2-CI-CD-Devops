package id.ac.ui.cs.advprog.eshop.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductValidatorTest {

    @Test
    void isQuantityInteger_whenValidInteger_returnsTrue() {
        assertTrue(ProductValidator.isQuantityInteger("10"));
    }

    @Test
    void isQuantityInteger_whenNegativeInteger_returnsTrue() {
        assertTrue(ProductValidator.isQuantityInteger("-5"));
    }

    @Test
    void isQuantityInteger_whenZero_returnsTrue() {
        assertTrue(ProductValidator.isQuantityInteger("0"));
    }

    @Test
    void isQuantityInteger_whenNull_returnsFalse() {
        assertFalse(ProductValidator.isQuantityInteger(null));
    }

    @Test
    void isQuantityInteger_whenFloat_returnsFalse() {
        assertFalse(ProductValidator.isQuantityInteger("1.5"));
    }

    @Test
    void isQuantityInteger_whenNonNumeric_returnsFalse() {
        assertFalse(ProductValidator.isQuantityInteger("abc"));
    }

    @Test
    void isQuantityInteger_whenEmpty_returnsFalse() {
        assertFalse(ProductValidator.isQuantityInteger(""));
    }

    @Test
    void isQuantityPositive_whenPositive_returnsTrue() {
        assertTrue(ProductValidator.isQuantityPositive(1));
        assertTrue(ProductValidator.isQuantityPositive(100));
    }

    @Test
    void isQuantityPositive_whenZeroOrNegative_returnsFalse() {
        assertFalse(ProductValidator.isQuantityPositive(0));
        assertFalse(ProductValidator.isQuantityPositive(-1));
    }

    @Test
    void integration_check_integerButNotPositive() {
        String input = "-1";
        assertTrue(ProductValidator.isQuantityInteger(input));
        int parsed = Integer.parseInt(input);
        assertFalse(ProductValidator.isQuantityPositive(parsed));
    }

}
