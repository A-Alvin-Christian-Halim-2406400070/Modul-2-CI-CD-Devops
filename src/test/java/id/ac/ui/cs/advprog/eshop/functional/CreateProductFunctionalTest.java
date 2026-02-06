package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter ;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value ;
import org.springframework.boot.test.context.SpringBootTest ;
import org.springframework.boot.test.web.server.LocalServerPort ;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT ;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
public class CreateProductFunctionalTest {
    @LocalServerPort
    private int serverPort;

    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;
    private String baseUrl;
    @BeforeEach
    void setUpTest() {
        baseUrl = String.format("%s:%d/product/list", testBaseUrl, serverPort);
    }

    @Test
    void testCreateValidProduct(ChromeDriver driver) throws Exception {
        driver.get(baseUrl);

        driver.findElement(By.id("createProductButton")).click();
        assertEquals(baseUrl.replace("/list", "/create"), driver.getCurrentUrl());

        driver.findElement(By.name("productName")).sendKeys("Sample Product");

        var qty = driver.findElement(By.name("productQuantity"));
        qty.clear();
        qty.sendKeys("10");
        driver.findElement(By.id("submitButton")).click();

        assertEquals(baseUrl, driver.getCurrentUrl());
        String createdProductName = driver.findElement(By.xpath("//table/tbody/tr[last()]/td[1]")).getText();
        String createdProductQuantity = driver.findElement(By.xpath("//table/tbody/tr[last()]/td[2]")).getText();
        assertEquals("Sample Product", createdProductName);
        assertEquals("10", createdProductQuantity);
    }

    @Test
    void testCreateProductWithStringQuantityShowsError(ChromeDriver driver) throws Exception {
        driver.get(baseUrl);

        driver.findElement(By.id("createProductButton")).click();
        assertEquals(baseUrl.replace("/list", "/create"), driver.getCurrentUrl());

        driver.findElement(By.name("productName")).sendKeys("Invalid Quantity Product");

        var qty = driver.findElement(By.name("productQuantity"));
        qty.clear();
        qty.sendKeys("ten");
        driver.findElement(By.id("submitButton")).click();

        assertEquals(baseUrl.replace("/list", "/create"), driver.getCurrentUrl());
        String errorMessage = driver.findElement(By.id("quantityError")).getText();
        assertEquals("Quantity must be an integer", errorMessage);
    }

    @Test
    void testCreateProductWithFloatQuantityShowsError(ChromeDriver driver) throws Exception {
        driver.get(baseUrl);

        driver.findElement(By.id("createProductButton")).click();
        assertEquals(baseUrl.replace("/list", "/create"), driver.getCurrentUrl());

        driver.findElement(By.name("productName")).sendKeys("Invalid Quantity Product");

        var qty = driver.findElement(By.name("productQuantity"));
        qty.clear();
        qty.sendKeys("10.5");
        driver.findElement(By.id("submitButton")).click();

        assertEquals(baseUrl.replace("/list", "/create"), driver.getCurrentUrl());
        String errorMessage = driver.findElement(By.id("quantityError")).getText();
        assertEquals("Quantity must be an integer", errorMessage);
    }

    @Test
    void testCreateProductWithNegativeQuantityShowsError(ChromeDriver driver) throws Exception {
        driver.get(baseUrl);

        driver.findElement(By.id("createProductButton")).click();
        assertEquals(baseUrl.replace("/list", "/create"), driver.getCurrentUrl());

        driver.findElement(By.name("productName")).sendKeys("Invalid Quantity Product");

        var qty = driver.findElement(By.name("productQuantity"));
        qty.clear();
        qty.sendKeys("-5");
        driver.findElement(By.id("submitButton")).click();

        assertEquals(baseUrl.replace("/list", "/create"), driver.getCurrentUrl());
        String errorMessage = driver.findElement(By.id("quantityError")).getText();
        assertEquals("Quantity has to be more than 0", errorMessage);
    }
    @Test
    void testCreateProductWithZeroQuantityShowsError(ChromeDriver driver) throws Exception {
        driver.get(baseUrl);

        driver.findElement(By.id("createProductButton")).click();
        assertEquals(baseUrl.replace("/list", "/create"), driver.getCurrentUrl());

        driver.findElement(By.name("productName")).sendKeys("Invalid Quantity Product");

        var qty = driver.findElement(By.name("productQuantity"));
        qty.clear();
        qty.sendKeys("0");
        driver.findElement(By.id("submitButton")).click();

        assertEquals(baseUrl.replace("/list", "/create"), driver.getCurrentUrl());
        String errorMessage = driver.findElement(By.id("quantityError")).getText();
        assertEquals("Quantity has to be more than 0", errorMessage);
    }
}
