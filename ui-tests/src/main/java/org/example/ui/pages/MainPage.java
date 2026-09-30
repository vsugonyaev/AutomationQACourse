package org.example.ui.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.example.common.config.TestConfig;

import static com.codeborne.selenide.Condition.clickable;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/** Витрина магазина: карточки товаров, корзина, оформление заказа. */
public class MainPage {

    private static final String PRODUCT_CARD = "div.product-card[data-name='%s']";
    private static final String ADD_TO_CART_BUTTON = "button[data-action='add-to-cart'][data-name='%s']";

    private final SelenideElement mainTitle = $("#main-title");
    private final SelenideElement cartButton = $("#open-cart-btn");
    private final SelenideElement cartCount = $("#cart-count");
    private final SelenideElement cartModal = $("#cartModal");
    private final SelenideElement totalPrice = $("#total-price");
    private final SelenideElement makeOrderButton = $("#makeOrder");
    private final ElementsCollection cartItemNames = $$("#cart-items .cart-item b");
    private final ElementsCollection toasts = $$("#toast-container .toast");

    @Step("Открыть витрину магазина")
    public MainPage open() {
        Selenide.open(TestConfig.storefrontPath());
        return this;
    }

    @Step("Обновить страницу витрины")
    public MainPage refresh() {
        Selenide.refresh();
        return this;
    }

    @Step("Нажать 'В корзину' у товара '{productName}'")
    public MainPage clickAddToCart(String productName) {
        $(String.format(ADD_TO_CART_BUTTON, productName)).shouldBe(clickable).click();
        return this;
    }

    @Step("Открыть корзину")
    public MainPage openCart() {
        cartButton.shouldBe(clickable).click();
        cartModal.shouldBe(visible);
        return this;
    }

    @Step("Нажать 'Оформить заказ'")
    public MainPage clickMakeOrder() {
        makeOrderButton.shouldBe(clickable).click();
        return this;
    }

    @Step("Прочитать текст всплывающего окна (alert)")
    public String alertText() {
        return Selenide.switchTo().alert().getText();
    }

    @Step("Закрыть всплывающее окно (alert)")
    public MainPage acceptAlert() {
        Selenide.switchTo().alert().accept();
        return this;
    }

    public SelenideElement productCard(String productName) {
        return $(String.format(PRODUCT_CARD, productName));
    }

    public SelenideElement mainTitle() {
        return mainTitle;
    }

    public SelenideElement cartCount() {
        return cartCount;
    }

    public SelenideElement totalPrice() {
        return totalPrice;
    }

    public ElementsCollection cartItemNames() {
        return cartItemNames;
    }

    public ElementsCollection toasts() {
        return toasts;
    }
}
