package org.example.UITests;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class UIUtils {
    static WebDriver driver = new ChromeDriver();
    public static final String BASE_URL = "http://localhost:8080/";
    public static final String ADMIN_URL = "http://localhost:8080/admin";


    public static void init () {
        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        options.addArguments("--disable-notifications");
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);
    }
    public static void closeAlert() {
        driver.get(ADMIN_URL);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        alert.dismiss();
    }
    public static void auth () {
        closeAlert();
        WebElement usernameInput = driver.findElement(By.id("username"));
        usernameInput.sendKeys("admin");
        WebElement passwordInput = driver.findElement(By.id("password"));
        passwordInput.sendKeys("secret123");
        }
    public static void createNewProduct (String name, double price) {
        auth();
        WebElement productNameInput = driver.findElement(By.id("n-name"));
        productNameInput.sendKeys(name);
        WebElement productPriceInput = driver.findElement(By.id("n-price"));
        productPriceInput.sendKeys("price");
        WebElement createBtn = driver.findElement(By.id("add-btn"));
        createBtn.click();
    }
    public static void deleteCreatedProduct(String name) {
        auth();
        String xpathExpression = String.format(
                "//tr[td/input[@value='%s']]//button[contains(@class, 'btn-del')]",
                name
        );
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement deleteBtn = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath(xpathExpression))
        );
        deleteBtn.click();
    }
    public static void hardRefresh(String url) {
        driver.manage().deleteAllCookies();
        driver.manage().deleteAllCookies();
        if (driver instanceof org.openqa.selenium.JavascriptExecutor) {
            org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
            js.executeScript("window.localStorage.clear();");
            js.executeScript("window.sessionStorage.clear();");
        }
        driver.get(url);
    }
}
