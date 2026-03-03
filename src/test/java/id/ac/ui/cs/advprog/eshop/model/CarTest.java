package id.ac.ui.cs.advprog.eshop.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CarTest {

    private Car car;

    @BeforeEach
    void setUp() {
        car = new Car();
    }

    @Test
    void testGetAndSetCarId() {
        car.setCarId("car-123");
        assertEquals("car-123", car.getCarId());
    }

    @Test
    void testGetAndSetCarName() {
        car.setCarName("Toyota");
        assertEquals("Toyota", car.getCarName());
    }

    @Test
    void testGetAndSetCarColor() {
        car.setCarColor("Red");
        assertEquals("Red", car.getCarColor());
    }

    @Test
    void testGetAndSetCarQuantity() {
        car.setCarQuantity(10);
        assertEquals(10, car.getCarQuantity());
    }

    @Test
    void testDefaultValuesAreNull() {
        Car newCar = new Car();
        assertNull(newCar.getCarId());
        assertNull(newCar.getCarName());
        assertNull(newCar.getCarColor());
        assertEquals(0, newCar.getCarQuantity());
    }

    @Test
    void testSetAllProperties() {
        car.setCarId("car-456");
        car.setCarName("Honda");
        car.setCarColor("Blue");
        car.setCarQuantity(5);

        assertEquals("car-456", car.getCarId());
        assertEquals("Honda", car.getCarName());
        assertEquals("Blue", car.getCarColor());
        assertEquals(5, car.getCarQuantity());
    }
}
