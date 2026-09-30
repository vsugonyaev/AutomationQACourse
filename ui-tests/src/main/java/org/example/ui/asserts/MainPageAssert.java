package org.example.ui.asserts;

import com.codeborne.selenide.CollectionCondition;
import io.qameta.allure.Step;
import org.assertj.core.api.Assertions;
import org.example.ui.pages.MainPage;
import org.example.ui.util.PriceFormat;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

public class MainPageAssert {

    private static final String PRICE_ATTRIBUTE = "data-price";

    private final MainPage mainPage;

    public MainPageAssert(MainPage mainPage) {
        this.mainPage = mainPage;
    }

    @Step("Проверить, что на витрине есть карточка товара '{productName}'")
    public MainPageAssert productCardIsVisible(String productName) {
        mainPage.productCard(productName).shouldBe(visible);
        return this;
    }

    @Step("Проверить, что цена товара '{productName}' на витрине = {price}")
    public MainPageAssert productCardHasPrice(String productName, double price) {
        mainPage.productCard(productName).shouldHave(attribute(PRICE_ATTRIBUTE, PriceFormat.asShown(price)));
        return this;
    }

    @Step("Проверить, что счётчик корзины = {expectedCount}")
    public MainPageAssert cartCountIs(int expectedCount) {
        mainPage.cartCount().shouldHave(exactText(String.valueOf(expectedCount)));
        return this;
    }

    @Step("Проверить, что в корзине есть товар '{productName}'")
    public MainPageAssert cartContains(String productName) {
        mainPage.cartItemNames().findBy(exactText(productName)).shouldBe(visible);
        return this;
    }

    @Step("Проверить, что в корзине {expectedCount} поз.")
    public MainPageAssert cartItemsCountIs(int expectedCount) {
        mainPage.cartItemNames().shouldHave(CollectionCondition.size(expectedCount));
        return this;
    }

    @Step("Проверить, что сумма корзины содержит '{expectedTotal}'")
    public MainPageAssert totalPriceHasText(String expectedTotal) {
        mainPage.totalPrice().shouldHave(text(expectedTotal));
        return this;
    }

    @Step("Проверить уведомление '{expectedText}'")
    public MainPageAssert toastIsShown(String expectedText) {
        mainPage.toasts().findBy(exactText(expectedText)).shouldBe(visible);
        return this;
    }

    @Step("Проверить заголовок витрины: '{expectedTitle}'")
    public MainPageAssert mainTitleIs(String expectedTitle) {
        mainPage.mainTitle().shouldHave(exactText(expectedTitle));
        return this;
    }

    @Step("Проверить текст всплывающего окна (alert): '{expectedText}'")
    public MainPageAssert alertTextIs(String expectedText) {
        Assertions.assertThat(mainPage.alertText())
                .as("Текст alert")
                .isEqualTo(expectedText);
        return this;
    }
}
