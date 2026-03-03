package id.ac.ui.cs.advprog.eshop.repository;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import id.ac.ui.cs.advprog.eshop.model.Car;

class CarRepositoryTest {

    private CarRepository carRepository;

    @BeforeEach
    void setUp() {
        carRepository = new CarRepository();
    }

    @Test
    void testCreateWithNullIdGeneratesUUID() {
        Car car = new Car();
        car.setCarName("Toyota");
        car.setCarColor("Red");
        car.setCarQuantity(5);

        Car created = carRepository.create(car);

        assertNotNull(created.getCarId());
        assertEquals("Toyota", created.getCarName());
        assertEquals("Red", created.getCarColor());
        assertEquals(5, created.getCarQuantity());
    }

    @Test
    void testCreateWithExistingIdKeepsId() {
        Car car = new Car();
        car.setCarId("existing-id-123");
        car.setCarName("Honda");
        car.setCarColor("Blue");
        car.setCarQuantity(3);

        Car created = carRepository.create(car);

        assertEquals("existing-id-123", created.getCarId());
        assertEquals("Honda", created.getCarName());
    }

    @Test
    void testFindAllEmpty() {
        Iterator<Car> iterator = carRepository.findAll();
        assertFalse(iterator.hasNext());
    }

    @Test
    void testFindAllWithOneCar() {
        Car car = new Car();
        car.setCarId("car-1");
        car.setCarName("Toyota");
        car.setCarColor("Red");
        car.setCarQuantity(5);

        carRepository.create(car);

        Iterator<Car> iterator = carRepository.findAll();
        assertTrue(iterator.hasNext());
        Car found = iterator.next();
        assertEquals("car-1", found.getCarId());
        assertEquals("Toyota", found.getCarName());
        assertFalse(iterator.hasNext());
    }

    @Test
    void testFindAllWithMultipleCars() {
        Car car1 = new Car();
        car1.setCarId("car-1");
        car1.setCarName("Toyota");
        car1.setCarColor("Red");
        car1.setCarQuantity(5);

        Car car2 = new Car();
        car2.setCarId("car-2");
        car2.setCarName("Honda");
        car2.setCarColor("Blue");
        car2.setCarQuantity(3);

        carRepository.create(car1);
        carRepository.create(car2);

        Iterator<Car> iterator = carRepository.findAll();
        assertTrue(iterator.hasNext());
        Car first = iterator.next();
        assertEquals("car-1", first.getCarId());

        assertTrue(iterator.hasNext());
        Car second = iterator.next();
        assertEquals("car-2", second.getCarId());

        assertFalse(iterator.hasNext());
    }

    @Test
    void testFindByIdFound() {
        Car car = new Car();
        car.setCarId("car-123");
        car.setCarName("Toyota");
        car.setCarColor("Red");
        car.setCarQuantity(5);

        carRepository.create(car);

        Car found = carRepository.findById("car-123");

        assertNotNull(found);
        assertEquals("car-123", found.getCarId());
        assertEquals("Toyota", found.getCarName());
        assertEquals("Red", found.getCarColor());
        assertEquals(5, found.getCarQuantity());
    }

    @Test
    void testFindByIdNotFound() {
        Car car = new Car();
        car.setCarId("car-123");
        car.setCarName("Toyota");
        car.setCarColor("Red");
        car.setCarQuantity(5);

        carRepository.create(car);

        Car found = carRepository.findById("nonexistent-id");

        assertNull(found);
    }

    @Test
    void testFindByIdEmptyRepository() {
        Car found = carRepository.findById("any-id");
        assertNull(found);
    }

    @Test
    void testUpdateFound() {
        Car car = new Car();
        car.setCarId("car-123");
        car.setCarName("Toyota");
        car.setCarColor("Red");
        car.setCarQuantity(5);

        carRepository.create(car);

        Car updatedCar = new Car();
        updatedCar.setCarName("Updated Toyota");
        updatedCar.setCarColor("Green");
        updatedCar.setCarQuantity(10);

        Car result = carRepository.update("car-123", updatedCar);

        assertNotNull(result);
        assertEquals("car-123", result.getCarId());
        assertEquals("Updated Toyota", result.getCarName());
        assertEquals("Green", result.getCarColor());
        assertEquals(10, result.getCarQuantity());

        // Verify the change persisted
        Car found = carRepository.findById("car-123");
        assertEquals("Updated Toyota", found.getCarName());
        assertEquals("Green", found.getCarColor());
        assertEquals(10, found.getCarQuantity());
    }

    @Test
    void testUpdateNotFound() {
        Car updatedCar = new Car();
        updatedCar.setCarName("New Car");
        updatedCar.setCarColor("Blue");
        updatedCar.setCarQuantity(3);

        Car result = carRepository.update("nonexistent-id", updatedCar);

        assertNull(result);
    }

    @Test
    void testUpdateWithMultipleCarsUpdatesCorrectOne() {
        Car car1 = new Car();
        car1.setCarId("car-1");
        car1.setCarName("Toyota");
        car1.setCarColor("Red");
        car1.setCarQuantity(5);

        Car car2 = new Car();
        car2.setCarId("car-2");
        car2.setCarName("Honda");
        car2.setCarColor("Blue");
        car2.setCarQuantity(3);

        carRepository.create(car1);
        carRepository.create(car2);

        Car updatedCar = new Car();
        updatedCar.setCarName("Updated Honda");
        updatedCar.setCarColor("Black");
        updatedCar.setCarQuantity(7);

        carRepository.update("car-2", updatedCar);

        // car-1 should remain unchanged
        Car foundCar1 = carRepository.findById("car-1");
        assertEquals("Toyota", foundCar1.getCarName());
        assertEquals("Red", foundCar1.getCarColor());
        assertEquals(5, foundCar1.getCarQuantity());

        // car-2 should be updated
        Car foundCar2 = carRepository.findById("car-2");
        assertEquals("Updated Honda", foundCar2.getCarName());
        assertEquals("Black", foundCar2.getCarColor());
        assertEquals(7, foundCar2.getCarQuantity());
    }

    @Test
    void testDeleteExisting() {
        Car car = new Car();
        car.setCarId("car-123");
        car.setCarName("Toyota");
        car.setCarColor("Red");
        car.setCarQuantity(5);

        carRepository.create(car);

        carRepository.delete("car-123");

        Car found = carRepository.findById("car-123");
        assertNull(found);

        Iterator<Car> iterator = carRepository.findAll();
        assertFalse(iterator.hasNext());
    }

    @Test
    void testDeleteNonExisting() {
        Car car = new Car();
        car.setCarId("car-123");
        car.setCarName("Toyota");
        car.setCarColor("Red");
        car.setCarQuantity(5);

        carRepository.create(car);

        // Should not throw exception
        carRepository.delete("nonexistent-id");

        // Original car should still exist
        Car found = carRepository.findById("car-123");
        assertNotNull(found);
    }

    @Test
    void testDeleteFromEmptyRepository() {
        // Should not throw exception
        carRepository.delete("any-id");

        Iterator<Car> iterator = carRepository.findAll();
        assertFalse(iterator.hasNext());
    }

    @Test
    void testDeleteWithMultipleCarsDeletesCorrectOne() {
        Car car1 = new Car();
        car1.setCarId("car-1");
        car1.setCarName("Toyota");
        car1.setCarColor("Red");
        car1.setCarQuantity(5);

        Car car2 = new Car();
        car2.setCarId("car-2");
        car2.setCarName("Honda");
        car2.setCarColor("Blue");
        car2.setCarQuantity(3);

        carRepository.create(car1);
        carRepository.create(car2);

        carRepository.delete("car-1");

        assertNull(carRepository.findById("car-1"));
        assertNotNull(carRepository.findById("car-2"));

        Iterator<Car> iterator = carRepository.findAll();
        assertTrue(iterator.hasNext());
        Car remaining = iterator.next();
        assertEquals("car-2", remaining.getCarId());
        assertFalse(iterator.hasNext());
    }
}
