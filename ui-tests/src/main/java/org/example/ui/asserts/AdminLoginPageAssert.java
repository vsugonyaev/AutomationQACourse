package org.example.ui.asserts;

import io.qameta.allure.Step;
import org.example.ui.pages.AdminLoginPage;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

public class AdminLoginPageAssert {

    private final AdminLoginPage loginPage;

    public AdminLoginPageAssert(AdminLoginPage loginPage) {
        this.loginPage = loginPage;
    }

    @Step("Проверить, что отображается форма входа")
    public AdminLoginPageAssert loginFormIsVisible() {
        loginPage.usernameInput().shouldBe(visible);
        loginPage.passwordInput().shouldBe(visible);
        loginPage.signInButton().shouldBe(visible);
        return this;
    }

    @Step("Проверить сообщение об ошибке входа: '{expectedMessage}'")
    public AdminLoginPageAssert errorMessageIs(String expectedMessage) {
        loginPage.errorAlert().shouldBe(visible).shouldHave(text(expectedMessage));
        return this;
    }
}
