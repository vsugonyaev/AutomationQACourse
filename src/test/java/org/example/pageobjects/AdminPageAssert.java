package org.example.pageobjects;

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

    public AdminPageAssert newNameFieldIsVisible() {
        adminPage.newNameInput().shouldBe(visible);
        return this;
    }

    public AdminPageAssert newPriceFieldIsVisible() {
        adminPage.newPriceInput().shouldBe(visible);
        return this;
    }

    public AdminPageAssert addButtonIsVisible() {
        adminPage.addButton().shouldBe(visible);
        return this;
    }

    public AdminPageAssert newNameFieldHasValue(String expected) {
        adminPage.newNameInput().shouldHave(value(expected));
        return this;
    }

    public AdminPageAssert newPriceFieldHasValue(String expected) {
        adminPage.newPriceInput().shouldHave(value(expected));
        return this;
    }

    public AdminPageAssert lastToastHasText(String expectedText) {
        adminPage.lastToast().shouldBe(visible).shouldHave(text(expectedText));
        return this;
    }

    public AdminPageAssert lastToastMatches(String regex) {
        adminPage.lastToast().shouldBe(visible).shouldHave(matchText(regex));
        return this;
    }

    public AdminPageAssert productRowIsVisible(String name) {
        adminPage.productRow(name).shouldBe(visible);
        return this;
    }

    public AdminPageAssert productRowIsHidden(String name) {
        adminPage.productRow(name).shouldBe(hidden);
        return this;
    }
}
