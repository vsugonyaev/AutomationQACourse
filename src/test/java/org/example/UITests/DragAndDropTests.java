package org.example.UITests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.DragAndDropOptions;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static org.example.UITests.SelenideUIUtils.*;

@Tag("Theme7")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DragAndDropTests {

    @BeforeAll
    static void setUp() {
        System.out.println("==============================");
        System.out.println("Test method start");
        init();
        open(BASE_URL);
        System.out.println("Открываем главную страницу магазина.");
    }

    @AfterAll
    static void tearDown() {
        System.out.println("Все тесты закончили. Запускаем очистку..");
        deleteAllCreatedProducts();
        closeWebDriver();
        System.out.println("Test method end");
        System.out.println("==============================");
    }

    @Test
    @DisplayName("1.1 Перетащить элемент в корзину с помощью Drag-and-Drop.")
    @Order(1)
    void dragAndDropTest() {
        createNewProductThroughApi("Тестовый товар", 95);
        System.out.println("Создали новый товар по апи.");
        sleep(1000);
        refresh();
        System.out.println("Обновили страницу, чтобы товар появился на странице.");
        SelenideElement productCard = $(".product-card");
        SelenideElement cartBtn = $("#open-cart-btn");
        productCard.dragAndDrop(DragAndDropOptions.to(cartBtn));
        System.out.println("Перетащили карточку товара в кнопку корзины.");
        $("#cart-count")
                .as("Счетчик на кнопке корзины не увеличился.")
                .shouldHave(text("1"));
        System.out.println("Завершили тест добавления товара с помощью Drag-and-Drop.");
    }
    @Order(2)
    @Test
    @DisplayName("1.2 Удалить добавленный элемент из корзины, и проверить что он там больше не отображается")
    void deleteAddedProductFromCart() {
        SelenideElement cartBtn = $("#open-cart-btn");
        cartBtn.click();
        System.out.println("Кликнули по кнопке корзины.");
        $("[data-action='remove']")
                .as("Кнопка удаления товара из корзины не повяилась.")
                .shouldBe(Condition.visible).click();
        System.out.println("Удалили товар из корзины.");
        $(".cart-item")
                .as("Карточка товара не удалилась из корзины.")
                .shouldNotBe(Condition.exist);
        System.out.println("Проверили что карточка товара удалилась из корзины.");
        $(".modal-content")
                .as("Сумма после удаления не уменьшилась.")
                .shouldHave(Condition.text("Сумма: 0 ₽"));
        System.out.println("Проверили что сумма в корзине уменьшилась до 0.");
        System.out.println("Завершили группу тестов.");
    }
}
