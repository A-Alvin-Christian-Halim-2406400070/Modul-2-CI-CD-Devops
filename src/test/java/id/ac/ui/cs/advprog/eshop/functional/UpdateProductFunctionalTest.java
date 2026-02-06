package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter ;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value ;
import org.springframework.boot.test.context.SpringBootTest ;
import org.springframework.boot.test.web.server.LocalServerPort ;
import org.springframework.beans.factory.annotation.Autowired;

import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT ;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class UpdateProductFunctionalTest {
    @LocalServerPort
    private int serverPort;

    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;
    private String baseUrl;

    @Autowired
    private ProductRepository productRepository;
    private ChromeDriver driver;

    @BeforeEach
    void setUpTest(ChromeDriver driver) {
        if (productRepository != null) {
            productRepository.clear();
        }
        this.driver = driver;
        baseUrl = String.format("%s:%d/product/list", testBaseUrl, serverPort);
    }

    @AfterEach
    void tearDown() {
        if (productRepository != null) {
            productRepository.clear();
        }
        if (this.driver != null) {
            try {
                this.driver.quit();
            } catch (Exception ignored) {
            } finally {
                this.driver = null;
            }
        }
    }

    @Test
    void testUpdateProductWhenThereIsOnlyOneProduct() {
        driver.get(baseUrl);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        driver.findElement(By.id("createProductButton")).click();

        driver.findElement(By.name("productName")).sendKeys("Sample Product");

        var qty = driver.findElement(By.name("productQuantity"));
        qty.clear();
        qty.sendKeys("10");
        driver.findElement(By.id("submitButton")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table//td[normalize-space()='Sample Product']")));

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//table//tr[td[1][normalize-space()='Sample Product']]//a[contains(@id,'edit-btn-')]"))).click();

        var nameAfterUpdate = driver.findElement(By.name("productName"));
        nameAfterUpdate.clear();
        nameAfterUpdate.sendKeys("Update Product");
        var qtyAfterUpdate = driver.findElement(By.name("productQuantity"));
        qtyAfterUpdate.clear();
        qtyAfterUpdate.sendKeys("12");
        driver.findElement(By.id("submitButton")).click();

        WebElement updatedNameElem = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//table//tr[td[1][normalize-space()='Update Product']]/td[1]"))
        );
        WebElement updatedQtyElem = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//table//tr[td[1][normalize-space()='Update Product']]/td[2]"))
        );
        String updatedProductName = updatedNameElem.getText();
        String updatedProductQuantity = updatedQtyElem.getText();
        assertEquals("Update Product", updatedProductName);
        assertEquals("12", updatedProductQuantity);
    }

    @Test
    void testUpdateSecondProductWhenMultipleProductsExist() {
        driver.get(baseUrl);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        driver.findElement(By.id("createProductButton")).click();
        driver.findElement(By.name("productName")).sendKeys("Product One");
        var qty1 = driver.findElement(By.name("productQuantity"));
        qty1.clear();
        qty1.sendKeys("5");
        driver.findElement(By.id("submitButton")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table//td[normalize-space()='Product One']")));

        driver.findElement(By.id("createProductButton")).click();
        driver.findElement(By.name("productName")).sendKeys("Product Two");
        var qty2 = driver.findElement(By.name("productQuantity"));
        qty2.clear();
        qty2.sendKeys("8");
        driver.findElement(By.id("submitButton")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table//td[normalize-space()='Product Two']")));

        WebElement editBtnElem = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//table//tr[td[1][normalize-space()='Product Two']]//a[contains(@id,'edit-btn-')]")
        ));
        String editBtnId = editBtnElem.getDomProperty("id");
        wait.until(ExpectedConditions.elementToBeClickable(By.id(editBtnId))).click();

        WebElement nameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("productName")));
        WebElement qtyField = driver.findElement(By.name("productQuantity"));
        String currentNameValue = nameField.getDomProperty("value");
        currentNameValue = currentNameValue == null ? "" : currentNameValue.trim();
        String currentQtyValue = qtyField.getDomProperty("value");
        currentQtyValue = currentQtyValue == null ? "" : currentQtyValue.trim();
        assertEquals("Product Two", currentNameValue);
        assertEquals("8", currentQtyValue);

        nameField.clear();
        nameField.sendKeys("Product Two Updated");
        qtyField.clear();
        qtyField.sendKeys("10");
        driver.findElement(By.id("submitButton")).click();
        WebElement updatedName = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//table//tr[td[1][normalize-space()='Product Two Updated']]/td[1]")
        ));
        WebElement updatedQty = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//table//tr[td[1][normalize-space()='Product Two Updated']]/td[2]")
        ));
        assertEquals("Product Two Updated", updatedName.getText());
        assertEquals("10", updatedQty.getText());
    }

    void testInvalidUpdateProductTemplate(String quantityInput, String expectedErrorMessage) {
        driver.get(baseUrl);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        driver.findElement(By.id("createProductButton")).click();
        driver.findElement(By.name("productName")).sendKeys("Product To Edit");
        var qty = driver.findElement(By.name("productQuantity"));
        qty.clear();
        qty.sendKeys("15");
        driver.findElement(By.id("submitButton")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table//td[normalize-space()='Product To Edit']")));

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//table//tr[td[1][normalize-space()='Product To Edit']]//a[contains(@id,'edit-btn-')]"))).click();

        var nameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("productName")));
        var qtyField = driver.findElement(By.name("productQuantity"));
        nameField.clear();
        nameField.sendKeys("Product To Edit");
        qtyField.clear();
        qtyField.sendKeys(quantityInput);
        driver.findElement(By.id("submitButton")).click();

        WebElement errorElem = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("quantityError"))
        );
        String actualErrorMessage = errorElem.getText();
        assertEquals(expectedErrorMessage, actualErrorMessage);
        String color = errorElem.getCssValue("color");
        String rgb = color.replace("rgba(", "").replace("rgb(", "").replace(")", "");
        String[] parts = rgb.split(",");
        if (parts.length < 3) {
            throw new AssertionError("Unexpected CSS color format: " + color);
        }
        int r;
        int g;
        int b;
        try {
            r = Integer.parseInt(parts[0].trim());
            g = Integer.parseInt(parts[1].trim());
            b = Integer.parseInt(parts[2].trim());
        } catch (NumberFormatException e) {
            throw new AssertionError("Failed to parse RGB color from CSS value: " + color, e);
        }
        assertTrue(r > g && r > b && r > 50, "Red channel is not dominant and sufficiently strong");
    }

    @Test
    void testInvalidUpdateProductQuantity_NegativeQuantity() {
        testInvalidUpdateProductTemplate("-5", "Quantity has to be more than 0");
    }

    @Test
    void testInvalidUpdateProductQuantity_StringQuantity() {
        testInvalidUpdateProductTemplate("abc", "Quantity must be an integer");
    }

    @Test
    void testInvalidUpdateProductQuantity_FloatQuantity() {
        testInvalidUpdateProductTemplate("7.8", "Quantity must be an integer");
    }

    @Test
    void testInvalidUpdateProductQuantity_ZeroQuantity() {
        testInvalidUpdateProductTemplate("0", "Quantity has to be more than 0");
    }
}
