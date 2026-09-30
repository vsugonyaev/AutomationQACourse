package org.example.pageobjects;

import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;

public class AdminLoginPageAssert {

    private final AdminLoginPage adminLoginPage;

    public AdminLoginPageAssert(AdminLoginPage adminLoginPage) {
        this.adminLoginPage = adminLoginPage;
    }

    @Step("Проверить, что поле логина видно на странице")
    public AdminLoginPageAssert usernameFieldIsVisible() {
        adminLoginPage.usernameInput().shouldBe(visible);
        return this;
    }

    @Step("Проверить, что поле пароля видно на странице")
    public AdminLoginPageAssert passwordFieldIsVisible() {
        adminLoginPage.passwordInput().shouldBe(visible);
        return this;
    }

    @Step("Проверить, что кнопка 'Войти' видна на странице")
    public AdminLoginPageAssert loginButtonIsVisible() {
        adminLoginPage.loginButton().shouldBe(visible);
        return this;
    }

    @Step("Проверить, что поле логина содержит значение {expected}")
    public AdminLoginPageAssert usernameFieldHasValue(String expected) {
        adminLoginPage.usernameInput().shouldHave(value(expected));
        return this;
    }

    @Step("Проверить, что поле пароля содержит ожидаемое значение")
    public AdminLoginPageAssert passwordFieldHasValue(String expected) {
        adminLoginPage.passwordInput().shouldHave(value(expected));
        return this;
    }

    @Step("Проверить, что сообщение об ошибке авторизации видно на странице")
    public AdminLoginPageAssert errorMessageIsVisible() {
        adminLoginPage.errorAlert().shouldBe(visible);
        return this;
    }

    @Step("Проверить текст сообщения об ошибке авторизации: {expected}")
    public AdminLoginPageAssert errorMessageHasText(String expected) {
        adminLoginPage.errorAlert().shouldBe(visible).shouldHave(text(expected));
        return this;
    }
}
