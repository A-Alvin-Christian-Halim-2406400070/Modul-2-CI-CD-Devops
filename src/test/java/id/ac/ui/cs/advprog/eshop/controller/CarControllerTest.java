package id.ac.ui.cs.advprog.eshop.controller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.service.CarService;

@ExtendWith(MockitoExtension.class)
class CarControllerTest {

    @Mock
    private CarService carService;

    @Mock
    private Model model;

    private CarController controller;

    private Car sampleCar;

    @BeforeEach
    void setUp() throws Exception {
        controller = new CarController();
        Field f = CarController.class.getDeclaredField("carservice");
        f.setAccessible(true);
        f.set(controller, carService);

        sampleCar = new Car();
        sampleCar.setCarId("car-123");
        sampleCar.setCarName("Toyota");
        sampleCar.setCarColor("Red");
        sampleCar.setCarQuantity(5);
    }

    @Test
    void testCreateCarPageAddsEmptyCarToModel() {
        String view = controller.createCarPage(model);

        assertEquals("CreateCar", view);
        verify(model).addAttribute(eq("car"), any(Car.class));
        verifyNoInteractions(carService);
    }

    @Test
    void testCreateCarPostCreatesCarAndRedirects() {
        when(carService.create(sampleCar)).thenReturn(sampleCar);

        String view = controller.createCarPost(sampleCar, model);

        assertEquals("redirect:listCar", view);
        verify(carService).create(sampleCar);
    }

    @Test
    void testCarListPageAddsAllCarsToModel() {
        List<Car> cars = new ArrayList<>();
        cars.add(sampleCar);

        Car car2 = new Car();
        car2.setCarId("car-456");
        car2.setCarName("Honda");
        car2.setCarColor("Blue");
        car2.setCarQuantity(3);
        cars.add(car2);

        when(carService.findAll()).thenReturn(cars);

        String view = controller.carListPage(model);

        assertEquals("CarList", view);
        verify(carService).findAll();
        verify(model).addAttribute("cars", cars);
    }

    @Test
    void testCarListPageWithEmptyList() {
        List<Car> cars = new ArrayList<>();
        when(carService.findAll()).thenReturn(cars);

        String view = controller.carListPage(model);

        assertEquals("CarList", view);
        verify(carService).findAll();
        verify(model).addAttribute("cars", cars);
    }

    @Test
    void testEditCarPageFindsCarAndAddsToModel() {
        when(carService.findById("car-123")).thenReturn(sampleCar);

        String view = controller.editCarPage("car-123", model);

        assertEquals("EditCar", view);
        verify(carService).findById("car-123");
        verify(model).addAttribute("car", sampleCar);
    }

    @Test
    void testEditCarPageWithNonexistentCar() {
        when(carService.findById("nonexistent")).thenReturn(null);

        String view = controller.editCarPage("nonexistent", model);

        assertEquals("EditCar", view);
        verify(carService).findById("nonexistent");
        verify(model).addAttribute("car", null);
    }

    @Test
    void testEditCarPostUpdatesCarAndRedirects() {
        String view = controller.editCarPost(sampleCar, model);

        assertEquals("redirect:listCar", view);
        verify(carService).update("car-123", sampleCar);
    }

    @Test
    void testDeleteCarDeletesAndRedirects() {
        String view = controller.deleteCar("car-123");

        assertEquals("redirect:listCar", view);
        verify(carService).deleteCarById("car-123");
    }

    @Test
    void testDeleteCarWithDifferentId() {
        String view = controller.deleteCar("another-car-id");

        assertEquals("redirect:listCar", view);
        verify(carService).deleteCarById("another-car-id");
    }
}
