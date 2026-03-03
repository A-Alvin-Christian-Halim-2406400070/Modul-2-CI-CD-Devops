package id.ac.ui.cs.advprog.eshop.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.repository.CarRepository;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarServiceImpl carService;

    private Car sampleCar;

    @BeforeEach
    void setUp() {
        sampleCar = new Car();
        sampleCar.setCarId("car-123");
        sampleCar.setCarName("Toyota");
        sampleCar.setCarColor("Red");
        sampleCar.setCarQuantity(5);
    }

    @Test
    void testCreateForwardsToRepositoryAndReturnsCar() {
        when(carRepository.create(sampleCar)).thenReturn(sampleCar);

        Car result = carService.create(sampleCar);

        assertEquals(sampleCar, result);
        verify(carRepository).create(sampleCar);
    }

    @Test
    void testFindAllConvertsIteratorToList() {
        List<Car> cars = new ArrayList<>();
        Car car2 = new Car();
        car2.setCarId("car-456");
        car2.setCarName("Honda");
        car2.setCarColor("Blue");
        car2.setCarQuantity(3);

        cars.add(sampleCar);
        cars.add(car2);

        Iterator<Car> iterator = cars.iterator();
        when(carRepository.findAll()).thenReturn(iterator);

        List<Car> result = carService.findAll();

        assertEquals(2, result.size());
        assertEquals(sampleCar.getCarId(), result.get(0).getCarId());
        assertEquals(car2.getCarId(), result.get(1).getCarId());
        verify(carRepository).findAll();
    }

    @Test
    void testFindAllWhenEmpty() {
        List<Car> cars = new ArrayList<>();
        Iterator<Car> iterator = cars.iterator();
        when(carRepository.findAll()).thenReturn(iterator);

        List<Car> result = carService.findAll();

        assertTrue(result.isEmpty());
        verify(carRepository).findAll();
    }

    @Test
    void testFindByIdForwardsToRepository() {
        when(carRepository.findById("car-123")).thenReturn(sampleCar);

        Car result = carService.findById("car-123");

        assertEquals(sampleCar, result);
        verify(carRepository).findById("car-123");
    }

    @Test
    void testFindByIdReturnsNullWhenNotFound() {
        when(carRepository.findById("nonexistent")).thenReturn(null);

        Car result = carService.findById("nonexistent");

        assertNull(result);
        verify(carRepository).findById("nonexistent");
    }

    @Test
    void testUpdateForwardsToRepository() {
        Car updatedCar = new Car();
        updatedCar.setCarName("Updated Toyota");
        updatedCar.setCarColor("Green");
        updatedCar.setCarQuantity(10);

        carService.update("car-123", updatedCar);

        verify(carRepository).update("car-123", updatedCar);
    }

    @Test
    void testDeleteCarByIdForwardsToRepository() {
        carService.deleteCarById("car-123");

        verify(carRepository).delete("car-123");
    }
}
