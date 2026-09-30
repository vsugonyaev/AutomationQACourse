package org.example.pageobjects;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.example.config.ConfigReader;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class MainPage {

    private final SelenideElement cartButton = $("#open-cart-btn");
    private final SelenideElement cartCount = $("#cart-count");
    private final SelenideElement totalPrice = $("#total-price");
    private final SelenideElement makeOrderButton = $("button#makeOrder");
    private final ElementsCollection productCards = $$("div.product-card");
    private final ElementsCollection cartItems = $$("#cart-items .cart-item");
    private final ElementsCollection toasts = $$("#toast-container .toast");

    public MainPage open() {
        Selenide.open(ConfigReader.getStandUrl());
        return this;
    }

    public SelenideElement productCard(String productName) {
        return $(String.format("div.product-card[data-name='%s']", productName));
    }

    private SelenideElement addToCartButton(String productName) {
        return $(String.format("button.btn[data-name='%s']", productName));
    }

    public MainPage clickAddToCart(String productName) {
        addToCartButton(productName).shouldBe(Condition.clickable).click();
        return this;
    }

    public MainPage clickOpenCart() {
        cartButton.shouldBe(Condition.clickable).click();
        return this;
    }

    public MainPage clickMakeOrder() {
        makeOrderButton.shouldBe(Condition.clickable).click();
        return this;
    }

    public MainPage clickRemoveFirstCartItem() {
        $("[data-action='remove']").shouldBe(Condition.clickable).click();
        return this;
    }

    public SelenideElement cartButton() {
        return cartButton;
    }

    public SelenideElement cartCount() {
        return cartCount;
    }

    public SelenideElement totalPrice() {
        return totalPrice;
    }

    public SelenideElement makeOrderButton() {
        return makeOrderButton;
    }

    public ElementsCollection productCards() {
        return productCards;
    }

    public ElementsCollection cartItems() {
        return cartItems;
    }

    public ElementsCollection toasts() {
        return toasts;
    }

    public SelenideElement lastToast() {
        return toasts.last();
    }
}
