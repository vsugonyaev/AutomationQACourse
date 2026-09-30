package org.example.UITests;

import org.example.apiTests.SharedGoodsData;
import org.example.config.ConfigReader;
import org.example.pageobjects.AdminPage;
import org.example.pageobjects.AdminPageAssert;
import org.example.pageobjects.MainPage;
import org.example.pageobjects.MainPageAssert;
import org.example.pageobjects.ShopSteps;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.Selenide.closeWebDriver;

@Tag("Theme9")
public class PageObjectShopTests {

    private final ShopSteps shopSteps = new ShopSteps();

    @BeforeEach
    void setUp() {
        System.out.println("==============================");
        System.out.println("Test method start");
        SelenideUIUtils.init();
    }

    @AfterEach
    void tearDown() {
        SharedGoodsData.deleteAllProducts();
        closeWebDriver();
        System.out.println("Test method end");
        System.out.println("==============================");
    }

    @Test
    @DisplayName("2.1. Добавить три единицы товара в корзину и оплатить их (общая стоимость не должна превышать 300 рублей). Проверить уведомление об обработке заказа.")
    void payThreeProductsUnder300Test() {
        String product1 = "Тестовый товар1";
        String product2 = "Тестовый товар2";
        String product3 = "Тестовый товар3";
        double price = 90.00;

        AdminPage adminPage = shopSteps.loginAsAdmin(ConfigReader.getAdminUsername(), ConfigReader.getAdminPassword());
        adminPage.addProduct(product1, price)
                .addProduct(product2, price)
                .addProduct(product3, price);

        MainPage mainPage = shopSteps.openShop();
        MainPageAssert mainPageAssert = new MainPageAssert(mainPage);

        mainPage.clickAddToCart(product1);
        mainPage.clickAddToCart(product2);
        mainPage.clickAddToCart(product3);
        mainPageAssert.cartCountHasText("3");

        shopSteps.payCart(mainPage);
        mainPageAssert.lastToastHasText("Заказ принят в обработку!");
    }

    @Test
    @DisplayName("2.2. Добавить в корзину несколько разных товаров и проверить, что общая цена в корзине считается корректно.")
    void cartTotalPriceIsCalculatedCorrectlyTest() {
        String product1 = "Тестовый товар1";
        String product2 = "Тестовый товар2";
        double price1 = 21.99;
        double price2 = 42.07;
        String expectedTotal = String.format("%.0f", price1 + price2);

        AdminPage adminPage = shopSteps.loginAsAdmin(ConfigReader.getAdminUsername(), ConfigReader.getAdminPassword());
        adminPage.addProduct(product1, price1)
                .addProduct(product2, price2);

        MainPage mainPage = shopSteps.openShop();
        MainPageAssert mainPageAssert = new MainPageAssert(mainPage);

        mainPage.clickAddToCart(product1);
        mainPage.clickAddToCart(product2);
        mainPageAssert.cartCountHasText("2");

        mainPage.clickOpenCart();
        mainPageAssert.totalPriceHasText(expectedTotal);
    }

    @Test
    @DisplayName("2.3. Войти в админку и добавить товар. Проверить уведомление после добавления товара.")
    void loginAndAddProductShowsNotificationTest() {
        String productName = "Тестовый товар1";
        double price = 50.00;

        AdminPage adminPage = shopSteps.loginAsAdmin(ConfigReader.getAdminUsername(), ConfigReader.getAdminPassword());
        AdminPageAssert adminPageAssert = new AdminPageAssert(adminPage);

        adminPage.addProduct(productName, price);
        adminPageAssert.lastToastHasText("Товар успешно добавлен!");
    }

    @Test
    @DisplayName("2.4. Войти в админку и отредактировать товар. Выйти на список товаров и проверить, что изменения применились.")
    void loginAndEditProductTest() {
        String productName = "Тестовый товар1";
        double price = 20.01;
        String newProductName = "Обновленное название";
        double newPrice = 101.03;

        AdminPage adminPage = shopSteps.loginAsAdmin(ConfigReader.getAdminUsername(), ConfigReader.getAdminPassword());
        AdminPageAssert adminPageAssert = new AdminPageAssert(adminPage);

        adminPage.addProduct(productName, price);
        adminPageAssert.lastToastHasText("Товар успешно добавлен!");

        shopSteps.renameProduct(adminPage, productName, newProductName);
        adminPageAssert.lastToastMatches("Товар.*обновлен");

        MainPage mainPage = shopSteps.openShop();
        MainPageAssert mainPageAssert = new MainPageAssert(mainPage);
        mainPageAssert.productCardIsVisible(newProductName);
        mainPageAssert.productCardHasPrice(newProductName, String.valueOf(price));

        adminPage.open();
        shopSteps.changeProductPrice(adminPage, newProductName, newPrice);
        adminPageAssert.lastToastMatches("Товар.*обновлен");

        mainPage.open();
        mainPageAssert.productCardHasPrice(newProductName, String.valueOf(newPrice));
    }
}
