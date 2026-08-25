package org.example.UITests;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static io.restassured.RestAssured.given;
import static org.example.UITests.SelenideUIUtils.*;

@Tag("Theme6.3")
public class Task3SelenideUITests {
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
    @DisplayName("3.1. Добавить три единицы товара в корзину и оплатить их (общая стоимость не должна превышать 300 рублей). Проверить уведомление об обработке заказа.")
    void addProductsBelow300AndCheckOrderNotificationTest() {
        String productName3 = "Тестовый товар3";
        double price = 90.00;

        createNewProduct(productName1, price);
        createNewProduct(productName2, price);
        createNewProduct(productName3, price);
        open(BASE_URL);

        $("button.btn[data-name='" + productName1 + "']").shouldBe(clickable).click();
        $$("#toast-container .toast")
                .get(0)
                .shouldBe(visible)
                .shouldHave(text(String.format("%s (1 шт.) добавлен в корзину", productName1)));
        $("button.btn[data-name='" + productName2 + "']").shouldBe(clickable).click();
        $$("#toast-container .toast")
                .get(1)
                .shouldBe(visible)
                .shouldHave(text(String.format("%s (1 шт.) добавлен в корзину", productName2)));
        $("button.btn[data-name='" + productName3 + "']").shouldBe(clickable).click();
        $$("#toast-container .toast")
                .get(2)
                .shouldBe(visible)
                .shouldHave(text(String.format("%s (1 шт.) добавлен в корзину", productName3)));

        $("#cart-count").shouldHave(text("3"));
        $("#open-cart-btn").shouldBe(clickable).click();
        $("button#makeOrder").shouldBe(clickable).click();
        $$("#toast-container .toast")
                .get(3)
                .shouldBe(visible)
                .shouldHave(text("Заказ принят в обработку!"));
    }

    @Test
    @DisplayName("3.2. Добавить в корзину несколько разных товаров и проверить, что общая цена в корзине считается корректно.")
    void checkIfSumInCartCorrectTest() {
        double price1 = 21.99;
        double price2 = 42.07;
        double expectedTotal = price1 + price2;
        createNewProduct(productName1, price1);
        createNewProduct(productName2, price2);
        open(BASE_URL);

        $("button.btn[data-name='" + productName1 + "']").shouldBe(clickable).click();
        $$("#toast-container .toast")
                .get(0)
                .shouldBe(visible)
                .shouldHave(text(String.format("%s (1 шт.) добавлен в корзину", productName1)));
        $("button.btn[data-name='" + productName2 + "']").shouldBe(clickable).click();
        $$("#toast-container .toast")
                .get(1)
                .shouldBe(visible)
                .shouldHave(text(String.format("%s (1 шт.) добавлен в корзину", productName2)));

        $("#cart-count").shouldHave(text("2"));
        $("#open-cart-btn").shouldBe(clickable).click();
        String expectedTotalStr = String.format("%.0f", expectedTotal);
        $("#total-price")
                .as("Сумма в корзине считается неправильно!")
                .shouldHave(text(expectedTotalStr));
    }

    @Test
    @DisplayName("3.3. Войти в админку и добавить товар. Проверить уведомление после добавления товара.")
    void checkCreateProductNotificationTest() {
        open(ADMIN_URL);
        $("#username").shouldBe(visible).sendKeys("admin");
        $("#password").sendKeys("secret123");
        $("button.primary").click();
        $("#n-name").shouldBe(visible).sendKeys(productName1);
        $("#n-price").sendKeys(String.valueOf(50));
        $("#add-btn").click();
        createdProducts.add(productName1);
        System.out.println("Нажимаем кнопку добавления товара");
        $(".toast")
                .shouldBe(visible)
                .shouldHave(text("Товар успешно добавлен!"));
        hardRefresh(ADMIN_URL);
    }

    @Test
    @DisplayName("3.4. Войти в админку и отредактировать товар. Выйти на список товаров и проверить, что изменения применились.")
    void refactorCreatedProduct() {
        double price = 20.01;
        String newProductName = "Обновленное название";
        double newPrice = 101.03;

        given()
                .baseUri("http://localhost:8080/")
                .auth()
                .basic("admin", "secret123")
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "name": "%s",
                          "price": %s
                        }
                        """.formatted(productName1, price))
                .log().body()
                .when()
                .post("/goods/add")
                .then()
                .log().body()
                .statusCode(200);
        open(ADMIN_URL);
        auth();
        $x(String.format("//tr[td/input[@value='%s']]//input[contains(@id, 'nm-')]", productName1))
                .setValue(newProductName);
        $x(String.format(
                "//tr[td/input[@value='%s']]//button[contains(@class, 'btn-upd')]",
                productName1)).click();
        $("#toast-container .toast")
                .shouldBe(visible)
                .shouldHave(matchText("Товар.*обновлен"));
        open(BASE_URL);
        String cardSelector = String.format("div.product-card[data-name='%s']", newProductName);
        $(cardSelector)
                .shouldBe(visible)
                .shouldHave(attribute("data-price", String.valueOf(price)));
        open(ADMIN_URL);
        $x(String.format("//tr[td/input[@value='%s']]//input[@type='number']", newProductName))
                .setValue(String.valueOf(newPrice));
        $x(String.format(
                "//tr[td/input[@value='%s']]//button[contains(@class, 'btn-upd')]",
                newProductName)).click();
        $("#toast-container .toast")
                .shouldBe(visible)
                .shouldHave(matchText("Товар.*обновлен"));
        open(BASE_URL);
        $(cardSelector)
                .shouldBe(visible)
                .shouldHave(attribute("data-price", String.valueOf(newPrice)));
        createdProducts.add(newProductName);
        hardRefresh(ADMIN_URL);
    }
}
