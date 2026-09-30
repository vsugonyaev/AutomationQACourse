package org.example.ui.asserts;

import io.qameta.allure.Step;
import org.example.ui.pages.AdminPage;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.exactValue;
import static com.codeborne.selenide.Condition.visible;

public class AdminPageAssert {

    private final AdminPage adminPage;

    public AdminPageAssert(AdminPage adminPage) {
        this.adminPage = adminPage;
    }

    @Step("Проверить, что отображается форма добавления товара")
    public AdminPageAssert addFormIsVisible() {
        adminPage.newNameInput().shouldBe(visible);
        adminPage.newPriceInput().shouldBe(visible);
        adminPage.addButton().shouldBe(visible);
        return this;
    }

    @Step("Проверить уведомление '{expectedText}'")
    public AdminPageAssert toastIsShown(String expectedText) {
        adminPage.toasts().findBy(exactText(expectedText)).shouldBe(visible);
        return this;
    }

    @Step("Проверить, что товар '{productName}' есть в таблице товаров")
    public AdminPageAssert productIsListed(String productName) {
        adminPage.productNameInputs().findBy(exactValue(productName)).shouldBe(visible);
        return this;
    }
}
