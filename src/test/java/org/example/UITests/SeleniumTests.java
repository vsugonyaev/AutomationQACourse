package org.example.UITests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.UITests.SeleniumUIUtils.*;

@Tag("Theme6.1")
public class SeleniumTests {
    private final String productName1 = "Тестовый товар1";

    @BeforeEach
    void setUpBrowser() {
        System.out.println("==============================");
        System.out.println("Test method start");
        init();
    }

    @AfterEach
    void tearDownBrowser() {
        deleteAllCreatedProducts();
        driver.quit();
        System.out.println("Test method end");
        System.out.println("==============================");
    }
    @Test
    @DisplayName("1.1. Добавить товар через админку, выйти на витрину и проверить, что товар отображается")
    void addProductAndMakeSureItsVisibleTest() {
        double productPrice = 15.00;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        createNewProduct(productName1, productPrice);
        driver.get(BASE_URL);
        String selector = String.format("div.product-card[data-name='%s']", productName1);
        WebElement newProductCard = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(selector)));

        assertThat(newProductCard.isDisplayed())
                .as("Карточка товара %s должна быть видима на странице", productName1)
                .isTrue();
        System.out.println("Увидели карточку товара на странице, все ок, идем дальше...");
        assertThat(newProductCard.getAttribute("data-price"))
                .as("Цена созданного товара должна совпадать с отображаемой")
                .isEqualTo(String.format("%.0f", productPrice));
        System.out.println("Проверили цену товара созданного товара, все ок.");
    }

    @Test
    @DisplayName("1.2. Добавить товар в корзину и проверить, что он отображается")
    void addCreatedProductToCartAndMakeSureItsVisibleTest() {
        double productPrice = 15.00;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        createNewProduct(productName1, productPrice);
        driver.get(BASE_URL);
        String selector = String.format("button.btn[data-name='%s']", productName1);
        WebElement addToCartBtn = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(selector))
        );
        addToCartBtn.click();
        WebElement cartBtn = driver.findElement(By.cssSelector("button#open-cart-btn"));
        cartBtn.click();
        String xpathSelector = String.format(
                "//div[@id='cart-items']//div[contains(@class, 'cart-item') and contains(., '%s')]",
                productName1);
        WebElement productInCart = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathSelector))
        );

        assertThat(productInCart.isDisplayed())
                .as("Товар с именем '%s' должен отображаться в списке внутри корзины", productName1)
                .isTrue();
        System.out.println("Увидели что товар добавился в корзину и отображается.");
    }

    @Test
    @DisplayName("1.3. Попытаться войти в админку с неверным логином и паролем.")
    void authInAdminPageWithInvalidCredits() {
        driver.get(ADMIN_URL);
        System.out.println("Начинаем проверку авторизации с неправильным логином");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement usernameInput = driver.findElement(By.id("username"));
        usernameInput.sendKeys("admin1");
        WebElement passwordInput = driver.findElement(By.id("password"));
        passwordInput.sendKeys("secret123");
        driver.findElement(By.cssSelector("button.primary")).click();
        By alertLocator = By.cssSelector("div.alert.alert-danger");
        WebElement errorAlert = wait.until(ExpectedConditions.visibilityOfElementLocated(alertLocator));
        assertThat(errorAlert.isDisplayed())
                .as("Алерт с ошибкой авторизации должен быть виден")
                .isTrue();
        assertThat(errorAlert.getText().trim())
                .as("Текст сообщения об ошибке")
                .isEqualTo("Неверные учетные данные пользователя");
        System.out.println("Закончили проверку авторизации с неправильным логином");
        hardRefresh(ADMIN_URL);
        System.out.println("Начинаем проверку авторизации с неверным паролем");
        WebElement usernameInput2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        WebElement passwordInput2 = driver.findElement(By.id("password"));

        usernameInput2.sendKeys("admin");
        passwordInput2.sendKeys("secret1234");
        driver.findElement(By.cssSelector("button.primary")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.alert.alert-danger")));

        assertThat(driver.findElement(By.cssSelector("div.alert.alert-danger")).getText().trim())
                .as("Текст сообщения об ошибке при неверном пароле")
                .isEqualTo("Неверные учетные данные пользователя");
        System.out.println("Закончили проверку авторизации с неправильным паролем");
    }

    @Test
    @DisplayName("1.4. Проверить сохранение товаров в корзине после обновления страницы.")
    void checkIfAddedInCartProductsStillThereAfterRefresh() {
        String productName2 = "Тестовый товар2";
        double productPrice1 = 15.00;
        double productPrice2 = 20.00;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        createNewProduct(productName1, productPrice1);
        createNewProduct(productName2, productPrice2);
        driver.get(BASE_URL);
        System.out.println("Ищем первый товар чтобы добавить в корзину");
        String selector1 = String.format("button.btn[data-name='%s']", productName1);
        WebElement addToCartBtn = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(selector1))
        );
        addToCartBtn.click();
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector("#cart-count"), "1"));
        System.out.println("Счетчик увеличился.");
        String selector2 = String.format("button.btn[data-name='%s']", productName2);
        WebElement addToCartBtn2 = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(selector2))
        );
        addToCartBtn2.click();
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector("#cart-count"), "2"));
        System.out.println("Успешно добавили второй товар в корзину.");
        driver.navigate().refresh();
        System.out.println("Обновили страницу.");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#cart-count")));
        WebElement cartBtn = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button#open-cart-btn")));
        cartBtn.click();
        System.out.println("Открыли корзину.");
        List<WebElement> itemsInCart = driver.findElements(
                By.cssSelector("#cart-items .cart-item")
        );

        assertThat(itemsInCart)
                .as("Корзина пуста! Так быть не должно.")
                .isNotEmpty();

        List<String> cartItemsTexts = itemsInCart.stream()
                .map(WebElement::getText)
                .toList();

        assertThat(cartItemsTexts)
                .as("В корзине нет наших товаров!")
                .contains(productName1, productName2);
    }
}
