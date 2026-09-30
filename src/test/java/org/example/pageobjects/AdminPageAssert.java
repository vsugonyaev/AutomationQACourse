package org.example.pageobjects;

import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.hidden;
import static com.codeborne.selenide.Condition.matchText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;

public class AdminPageAssert {

    private final AdminPage adminPage;

    public AdminPageAssert(AdminPage adminPage) {
        this.adminPage = adminPage;
    }

    @Step("Проверить, что поле имени нового товара видно на странице")
    public AdminPageAssert newNameFieldIsVisible() {
        adminPage.newNameInput().shouldBe(visible);
        return this;
    }

    @Step("Проверить, что поле цены нового товара видно на странице")
    public AdminPageAssert newPriceFieldIsVisible() {
        adminPage.newPriceInput().shouldBe(visible);
        return this;
    }

    @Step("Проверить, что кнопка добавления товара видна на странице")
    public AdminPageAssert addButtonIsVisible() {
        adminPage.addButton().shouldBe(visible);
        return this;
    }

    @Step("Проверить, что поле имени нового товара содержит значение {expected}")
    public AdminPageAssert newNameFieldHasValue(String expected) {
        adminPage.newNameInput().shouldHave(value(expected));
        return this;
    }

    @Step("Проверить, что поле цены нового товара содержит значение {expected}")
    public AdminPageAssert newPriceFieldHasValue(String expected) {
        adminPage.newPriceInput().shouldHave(value(expected));
        return this;
    }

    @Step("Проверить текст последнего уведомления: {expectedText}")
    public AdminPageAssert lastToastHasText(String expectedText) {
        adminPage.lastToast().shouldBe(visible).shouldHave(text(expectedText));
        return this;
    }

    @Step("Проверить, что текст последнего уведомления соответствует шаблону: {regex}")
    public AdminPageAssert lastToastMatches(String regex) {
        adminPage.lastToast().shouldBe(visible).shouldHave(matchText(regex));
        return this;
    }

    @Step("Проверить, что строка товара {name} видна в таблице")
    public AdminPageAssert productRowIsVisible(String name) {
        adminPage.productRow(name).shouldBe(visible);
        return this;
    }

    @Step("Проверить, что строка товара {name} скрыта/отсутствует в таблице")
    public AdminPageAssert productRowIsHidden(String name) {
        adminPage.productRow(name).shouldBe(hidden);
        return this;
    }
}
