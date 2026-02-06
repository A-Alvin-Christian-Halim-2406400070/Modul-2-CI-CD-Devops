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
public class DeleteProductFunctionalTest {
    @LocalServerPort
    private int serverPort;

    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;
    private String baseUrl;

    @Autowired
    private ProductRepository productRepository;

    private ChromeDriver driver;

    @BeforeEach
    void setUp(ChromeDriver driver) {
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
    public void testCreateOneThenDelete_TableEmpty() {
        driver.get(baseUrl);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        driver.findElement(By.id("createProductButton")).click();
        driver.findElement(By.name("productName")).sendKeys("ToDelete");
        var qty = driver.findElement(By.name("productQuantity"));
        qty.clear();
        qty.sendKeys("3");
        driver.findElement(By.id("submitButton")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table//td[normalize-space()='ToDelete']")));

        WebElement deleteBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//table//tr[td[1][normalize-space()='ToDelete']]//form//button[@type='submit']")
        ));
        deleteBtn.click();

        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//table//td[normalize-space()='ToDelete']")));
        int rows = driver.findElements(By.xpath("//table//tbody/tr")).size();
        assertEquals(0, rows, "Table should be empty after deleting the only product");
    }

    @Test
    public void testCreateTwoDeleteOne_TableHasRemaining() {
        driver.get(baseUrl);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        driver.findElement(By.id("createProductButton")).click();
        driver.findElement(By.name("productName")).sendKeys("KeepMe");
        var q1 = driver.findElement(By.name("productQuantity"));
        q1.clear();
        q1.sendKeys("4");
        driver.findElement(By.id("submitButton")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table//td[normalize-space()='KeepMe']")));

        driver.findElement(By.id("createProductButton")).click();
        driver.findElement(By.name("productName")).sendKeys("RemoveMe");
        var q2 = driver.findElement(By.name("productQuantity"));
        q2.clear();
        q2.sendKeys("7");
        driver.findElement(By.id("submitButton")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table//td[normalize-space()='RemoveMe']")));

        WebElement deleteBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//table//tr[td[1][normalize-space()='RemoveMe']]//form//button[@type='submit']")
        ));
        deleteBtn.click();

        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//table//td[normalize-space()='RemoveMe']")));
        var rows = driver.findElements(By.xpath("//table//tbody/tr"));
        assertEquals(1, rows.size(), "There should be one remaining product row");
        String remainingName = driver.findElement(By.xpath("//table//tbody/tr/td[1]")).getText();
        assertEquals("KeepMe", remainingName);
    }
}
