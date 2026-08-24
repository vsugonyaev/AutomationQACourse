package org.example.UITests;

import com.codeborne.selenide.CollectionCondition;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.example.UITests.SelenideUIUtils.*;

@Tag("Theme6.2")
public class SelenideTests {

    private final String productName1 = "Тестовый товар1";
    private final String productName2 = "Тестовый товар2";


    @BeforeEach
    void setUp() {
        System.out.println("==============================");
        System.out.println("Test method start");
        init();
    }

    @AfterEach
    void tearDown() {
        deleteAllCreatedProducts();
        closeWebDriver();
        System.out.println("Test method end");
        System.out.println("==============================");
    }

    @Test
    @DisplayName("2.1. Добавить товар через админку, выйти на витрину и проверить, что товар отображается")
    void addProductAndMakeSureItsVisibleTest() {
        double productPrice = 15.00;
        createNewProduct(productName1, productPrice);
        open(BASE_URL);
        String selector = String.format("div.product-card[data-name='%s']", productName1);
        $(selector).shouldBe(visible)
                .shouldHave(attribute("data-price", String.format("%.0f", productPrice)));
        System.out.println("Увидели карточку товара на странице, все ок.");

    }

    @Test
    @DisplayName("2.2. Добавить товар в корзину и проверить, что он отображается")
    void addCreatedProductToCartAndMakeSureItsVisibleTest() {
        double productPrice = 15.00;
        createNewProduct(productName1, productPrice);
        open(BASE_URL);
        String selector = String.format("button.btn[data-name='%s']", productName1);
        $(selector).shouldBe(clickable).click();
        $("#open-cart-btn").click();
        String xpathSelector = String.format(
                "//div[@id='cart-items']//div[contains(@class, 'cart-item') and contains(., '%s')]",
                productName1);
        $x(xpathSelector).shouldBe(visible);
        System.out.println("Увидели что товар добавился в корзину и отображается.");
    }

    @Test
    @DisplayName("2.3. Попытаться войти в админку с неверным логином и паролем.")
    void authInAdminPageWithInvalidCredits() {
        open(ADMIN_URL);
        System.out.println("Начинаем проверку авторизации с неправильным логином");
        $("#username").shouldBe(visible).sendKeys("admin1");
        $("#password").sendKeys("secret123");
        $("button.primary").click();
        $("div.alert.alert-danger").shouldBe(visible)
                .shouldHave(text("Неверные учетные данные пользователя"));
        System.out.println("Закончили проверку авторизации с неправильным логином");
        hardRefresh(ADMIN_URL);
        System.out.println("Начинаем проверку авторизации с неверным паролем");
        $("#username").shouldBe(visible).sendKeys("admin");
        $("#password").sendKeys("secret1234");
        $("button.primary").click();
        $("div.alert.alert-danger").shouldBe(visible)
                .shouldHave(text("Неверные учетные данные пользователя"));
        System.out.println("Закончили проверку авторизации с неправильным паролем");
    }

    @Test
    @DisplayName("2.4. Проверить сохранение товаров в корзине после обновления страницы.")
    void checkIfAddedInCartProductsStillThereAfterRefresh() {
        double productPrice1 = 15.00;
        double productPrice2 = 20.00;
        createNewProduct(productName1, productPrice1);
        hardRefresh(ADMIN_URL);
        createNewProduct(productName2, productPrice2);
        hardRefresh(BASE_URL);

        String selector1 = String.format("button.btn[data-name='%s']", productName1);
        $(selector1).shouldBe(clickable).click();
        $("#cart-count").shouldHave(text("1"));

        String selector2 = String.format("button.btn[data-name='%s']", productName2);
        $(selector2).shouldBe(clickable).click();
        $("#cart-count").shouldHave(text("2"));

        refresh();
        System.out.println("Обновили страницу.");
        $("#open-cart-btn").shouldBe(clickable).click();
        System.out.println("Открыли корзину.");
        $$("#cart-items .cart-item").shouldHave(CollectionCondition.sizeGreaterThan(0));
        $$("#cart-items .cart-item").filterBy(text(productName1)).shouldHave(CollectionCondition.size(1));
        $$("#cart-items .cart-item").filterBy(text(productName2)).shouldHave(CollectionCondition.size(1));
        $$("#cart-items .cart-item").shouldHave(CollectionCondition.size(2));
    }

    @Test
    @DisplayName("2.5. Добавить в корзину товаров более чем на 300 рублей и нажать на кнопку «Оформить заказ». Проверить, что отображается JS Alert.")
    void addProductsOver300AndCheckAlertTest() {
        String productName3 = "Тестовый товар3";
        double price = 150.00;

        createNewProduct(productName1, price);
        hardRefresh(ADMIN_URL);
        createNewProduct(productName2, price);
        hardRefresh(ADMIN_URL);
        createNewProduct(productName3, price);
        hardRefresh(BASE_URL);


        $("button.btn[data-name='" + productName1 + "']").shouldBe(clickable).click();
        $("button.btn[data-name='" + productName2 + "']").shouldBe(clickable).click();
        $("button.btn[data-name='" + productName3 + "']").shouldBe(clickable).click();

        $("#cart-count").shouldHave(text("3"));
        $("#open-cart-btn").shouldBe(clickable).click();
        $("button#makeOrder").shouldBe(clickable).click();

        String alertText = confirm();
        System.out.println("Alert появился, текст: " + alertText);
    }
}