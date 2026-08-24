package org.example.UITests;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SeleniumUIUtils {
    static WebDriver driver ;
    public static final String BASE_URL = "http://localhost:8080/";
    public static final String ADMIN_URL = "http://localhost:8080/admin";
    private static final List<String> createdProducts = new ArrayList<>();


    public static void init () {
        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        options.addArguments("--disable-notifications");
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);
    }
     public static void auth () {
        driver.get(ADMIN_URL);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement usernameInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("username"))
        );
        usernameInput.sendKeys("admin");
        WebElement passwordInput = driver.findElement(By.id("password"));
        passwordInput.sendKeys("secret123");
        driver.findElement(By.cssSelector("button.primary")).click();
        }

    public static void createNewProduct (String name, double price) {
        System.out.println("Поступил запрос на создание товара: " + name);
        auth();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement productNameInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("n-name"))
        );
        productNameInput.sendKeys(name);
        WebElement productPriceInput = driver.findElement(By.id("n-price"));
        productPriceInput.sendKeys(String.valueOf(price));
        WebElement createBtn = driver.findElement(By.id("add-btn"));
        createBtn.click();
        System.out.println("Товар успешно создан!");
        hardRefresh(ADMIN_URL);
    }
    public static void deleteCreatedProduct(String name) {
        driver.get(ADMIN_URL);
        System.out.println("Поступил запрос на удаление товара: " + name);
        String xpathExpression = String.format(
                "//tr[td/input[@value='%s']]//button[contains(@class, 'btn-del')]",
                name
        );
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement deleteBtn = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath(xpathExpression))
        );
        deleteBtn.click();
        org.openqa.selenium.Alert confirmAlert = wait.until(ExpectedConditions.alertIsPresent());
        confirmAlert.accept();
        By toastLocator = By.xpath("//*[text()='Товар удален']");
        WebElement toastNotification = wait.until(ExpectedConditions.visibilityOfElementLocated(toastLocator));

        assertThat(toastNotification.isDisplayed())
                .as("Всплывающее уведомление 'Товар удален' должно быть видно")
                .isTrue();

        By productRowLocator = By.xpath(String.format("//tr[td/input[@value='%s']]", name));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(productRowLocator));
        System.out.println("Товар успешно удален!");
    }
    public static void deleteAllCreatedProducts() {
        auth();
        System.out.println("Удаляем все созданные товары: " + createdProducts);
        for (int i = createdProducts.size() - 1; i >= 0; i--) {
            String name = createdProducts.get(i);
            deleteCreatedProduct(name);
            createdProducts.remove(i);
        }
        hardRefresh(ADMIN_URL);
    }

    public static void hardRefresh(String url) {
        driver.manage().deleteAllCookies();
        if (driver instanceof org.openqa.selenium.JavascriptExecutor) {
            org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
            js.executeScript("window.localStorage.clear();");
            js.executeScript("window.sessionStorage.clear();");
        }
        driver.get(url);
    }
}
