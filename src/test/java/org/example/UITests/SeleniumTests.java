package org.example.UITests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.example.UITests.UIUtils.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


public class SeleniumTests {

    @BeforeEach
    void setUpBrowser() {
        init();
    }

    @AfterEach
    void tearDownBrowser() {
        driver.quit();
    }
    @Test
    @DisplayName("1.1. Добавить товар через админку, выйти на витрину и проверить, что товар отображается")
    void addProductAndMakeSureItsVisibleTest() {
        String productName = "Тестовый товар";
        Double productPrice = 15.00;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        createNewProduct(productName, productPrice);
        driver.get(BASE_URL);
        String selector = String.format("div.product-card[data-name='%s']", productName);
        WebElement newProductCard = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(selector)));

        assertThat(newProductCard.isDisplayed())
                .as("Карточка товара %s должна быть видима на странице", productName)
                .isTrue();

        assertThat(newProductCard.getAttribute("data-price"))
                .as("Цена созданного товара должна совпадать с отображаемой")
                .isEqualTo(String.valueOf(productPrice));
        deleteCreatedProduct(productName);
    }

    @Test
    @DisplayName("1.2. Добавить товар в корзину и проверить, что он отображается")
    void addCreatedProductToCartAndMakeSureItsVisibleTest() {
        String productName = "Тестовый товар";
        Double productPrice = 15.00;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        createNewProduct(productName, productPrice);
        driver.get(BASE_URL);
        String selector = String.format("button.btn[data-name='%s']", productName);
        WebElement addToCartBtn = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(selector))
        );
        addToCartBtn.click();
        WebElement cartBtn = driver.findElement(By.cssSelector("button#open-cart-btn"));
        cartBtn.click();
        String xpathSelector = String.format(
                "//div[@id='cart-items']//div[contains(@class, 'cart-item') and contains(., '%s')]",
                productName);
        WebElement productInCart = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathSelector))
        );

        assertThat(productInCart.isDisplayed())
                .as("Товар с именем '%s' должен отображаться в списке внутри корзины", productName)
                .isTrue();
        deleteCreatedProduct(productName);
    }

    @Test
    @DisplayName("1.3. Попытаться войти в админку с неверным логином и паролем.")
    void authInAdminPageWithInvalidCredits() {
        closeAlert();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement usernameInput = driver.findElement(By.id("username"));
        usernameInput.sendKeys("admin1");
        By alertLocator = By.cssSelector("div.alert.alert-danger");
        WebElement errorAlert = wait.until(ExpectedConditions.visibilityOfElementLocated(alertLocator));

        assertThat(errorAlert.isDisplayed())
                .as("Алерт с ошибкой авторизации должен быть виден")
                .isTrue();

        assertThat(errorAlert.getText().trim())
                .as("Текст сообщения об ошибке")
                .isEqualTo("Неверные учетные данные пользователя");

        hardRefresh(ADMIN_URL);

        WebElement passwordInput = driver.findElement(By.id("password"));
        passwordInput.sendKeys("secret123");
        WebElement anotherErrorAlert = wait.until(ExpectedConditions.visibilityOfElementLocated(alertLocator));

        assertThat(anotherErrorAlert.isDisplayed())
                .as("Алерт с ошибкой авторизации должен быть виден")
                .isTrue();

        assertThat(anotherErrorAlert.getText().trim())
                .as("Текст сообщения об ошибке")
                .isEqualTo("Неверные учетные данные пользователя");
    }
}
