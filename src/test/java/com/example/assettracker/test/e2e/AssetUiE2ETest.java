package com.example.assettracker.test.e2e;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Requires the app to be running on http://localhost:8080
 * Start it first:  ./mvnw spring-boot:run
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AssetUiE2ETest {

    private static WebDriver driver;
    private static WebDriverWait wait;
    private static final String BASE = "http://localhost:8080/";

    @BeforeAll
    static void startBrowser() {
        // If use Chrome to test then modify the code to use ChromeOptions and ChromeDriver
        EdgeOptions options = new EdgeOptions();
        //options.addArguments("--headless=new");  // uncomment for headless
        options.addArguments("--window-size=1280,900");
        driver = new EdgeDriver(options);           // Selenium Manager auto-resolves driver
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterAll
    static void stopBrowser() {
        if (driver != null) driver.quit();
    }

    @Test
    @Order(1)
    void pageLoadsAndTableRenders() {
        driver.get(BASE);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("assetTable")));

        assertThat(driver.getTitle()).contains("Asset Tracker");
        assertThat(driver.findElement(By.tagName("h1")).getText()).isEqualTo("Asset Tracker");
    }

    @Test
    @Order(2)
    void addingAssetShowsItInTable() {
        driver.get(BASE);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("addForm")));

        String uniqueName = "Selenium Laptop " + System.currentTimeMillis();
        String uniqueSN = "SN-SEL-" + System.currentTimeMillis();

        driver.findElement(By.id("name")).sendKeys(uniqueName);
        driver.findElement(By.id("type")).sendKeys("LAPTOP");
        driver.findElement(By.id("status")).sendKeys("IN_USE");
        driver.findElement(By.id("serialNumber")).sendKeys(uniqueSN);
        driver.findElement(By.id("assignedTo")).sendKeys("Selenium Bot");
        driver.findElement(By.cssSelector("#addForm button")).click();

        // Wait for the success message AND the new row
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.id("message"), "Asset added."));

        wait.until(d -> {
            List<WebElement> rows = d.findElements(By.cssSelector("#assetTable tbody tr"));
            return rows.stream().anyMatch(r -> r.getText().contains(uniqueName));
        });

        assertThat(driver.findElement(By.id("message")).getText()).contains("Asset added.");
    }

    @Test
    @Order(3)
    void filterByStatusUpdatesTable() {
        driver.get(BASE);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("statusFilter")));

        WebElement filter = driver.findElement(By.id("statusFilter"));
        new org.openqa.selenium.support.ui.Select(filter).selectByValue("IN_USE");

        // change event triggers loadAssets(); wait for rows to reflect IN_USE only
        wait.until(d -> {
            List<WebElement> rows = d.findElements(By.cssSelector("#assetTable tbody tr"));
            return !rows.isEmpty() && rows.stream().allMatch(r -> r.getText().contains("IN_USE"));
        });
    }
}
