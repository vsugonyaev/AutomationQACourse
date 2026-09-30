package org.example.pageobjects;

import com.codeborne.selenide.CollectionCondition;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.matchText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

public class MainPageAssert {

    private final MainPage mainPage;

    public MainPageAssert(MainPage mainPage) {
        this.mainPage = mainPage;
    }

    @Step("Проверить, что карточка товара {productName} видна на странице")
    public MainPageAssert productCardIsVisible(String productName) {
        mainPage.productCard(productName).shouldBe(visible);
        return this;
    }

    @Step("Проверить, что кнопка корзины видна на странице")
    public MainPageAssert cartButtonIsVisible() {
        mainPage.cartButton().shouldBe(visible);
        return this;
    }

    @Step("Проверить, что кнопка оформления заказа видна на странице")
    public MainPageAssert makeOrderButtonIsVisible() {
        mainPage.makeOrderButton().shouldBe(visible);
        return this;
    }

    @Step("Проверить, что цена товара {productName} равна {expectedPrice}")
    public MainPageAssert productCardHasPrice(String productName, String expectedPrice) {
        mainPage.productCard(productName).shouldHave(attribute("data-price", expectedPrice));
        return this;
    }

    @Step("Проверить, что счётчик корзины показывает {expectedCount}")
    public MainPageAssert cartCountHasText(String expectedCount) {
        mainPage.cartCount().shouldHave(text(expectedCount));
        return this;
    }

    @Step("Проверить, что итоговая сумма в корзине равна {expectedTotal}")
    public MainPageAssert totalPriceHasText(String expectedTotal) {
        mainPage.totalPrice().shouldHave(text(expectedTotal));
        return this;
    }

    @Step("Проверить, что в корзине {expectedCount} товаров")
    public MainPageAssert cartItemsCountIs(int expectedCount) {
        mainPage.cartItems().shouldHave(CollectionCondition.size(expectedCount));
        return this;
    }

    @Step("Проверить, что в корзине есть товар {productName}")
    public MainPageAssert cartContainsProduct(String productName) {
        mainPage.cartItems().filterBy(text(productName)).shouldHave(CollectionCondition.size(1));
        return this;
    }

    @Step("Проверить текст последнего уведомления: {expectedText}")
    public MainPageAssert lastToastHasText(String expectedText) {
        mainPage.lastToast().shouldBe(visible).shouldHave(text(expectedText));
        return this;
    }

    @Step("Проверить, что текст последнего уведомления соответствует шаблону: {regex}")
    public MainPageAssert lastToastMatches(String regex) {
        mainPage.lastToast().shouldBe(visible).shouldHave(matchText(regex));
        return this;
    }
}
