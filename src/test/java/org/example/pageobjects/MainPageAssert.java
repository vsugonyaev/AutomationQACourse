package org.example.pageobjects;

import com.codeborne.selenide.CollectionCondition;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.matchText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

public class MainPageAssert {

    private final MainPage mainPage;

    public MainPageAssert(MainPage mainPage) {
        this.mainPage = mainPage;
    }

    public MainPageAssert productCardIsVisible(String productName) {
        mainPage.productCard(productName).shouldBe(visible);
        return this;
    }

    public MainPageAssert cartButtonIsVisible() {
        mainPage.cartButton().shouldBe(visible);
        return this;
    }

    public MainPageAssert makeOrderButtonIsVisible() {
        mainPage.makeOrderButton().shouldBe(visible);
        return this;
    }

    public MainPageAssert productCardHasPrice(String productName, String expectedPrice) {
        mainPage.productCard(productName).shouldHave(attribute("data-price", expectedPrice));
        return this;
    }

    public MainPageAssert cartCountHasText(String expectedCount) {
        mainPage.cartCount().shouldHave(text(expectedCount));
        return this;
    }

    public MainPageAssert totalPriceHasText(String expectedTotal) {
        mainPage.totalPrice().shouldHave(text(expectedTotal));
        return this;
    }

    public MainPageAssert cartItemsCountIs(int expectedCount) {
        mainPage.cartItems().shouldHave(CollectionCondition.size(expectedCount));
        return this;
    }

    public MainPageAssert cartContainsProduct(String productName) {
        mainPage.cartItems().filterBy(text(productName)).shouldHave(CollectionCondition.size(1));
        return this;
    }

    public MainPageAssert lastToastHasText(String expectedText) {
        mainPage.lastToast().shouldBe(visible).shouldHave(text(expectedText));
        return this;
    }

    public MainPageAssert lastToastMatches(String regex) {
        mainPage.lastToast().shouldBe(visible).shouldHave(matchText(regex));
        return this;
    }
}
