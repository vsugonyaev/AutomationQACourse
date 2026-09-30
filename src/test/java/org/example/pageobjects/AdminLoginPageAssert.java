package org.example.pageobjects;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;

public class AdminLoginPageAssert {

    private final AdminLoginPage adminLoginPage;

    public AdminLoginPageAssert(AdminLoginPage adminLoginPage) {
        this.adminLoginPage = adminLoginPage;
    }

    public AdminLoginPageAssert usernameFieldIsVisible() {
        adminLoginPage.usernameInput().shouldBe(visible);
        return this;
    }

    public AdminLoginPageAssert passwordFieldIsVisible() {
        adminLoginPage.passwordInput().shouldBe(visible);
        return this;
    }

    public AdminLoginPageAssert loginButtonIsVisible() {
        adminLoginPage.loginButton().shouldBe(visible);
        return this;
    }

    public AdminLoginPageAssert usernameFieldHasValue(String expected) {
        adminLoginPage.usernameInput().shouldHave(value(expected));
        return this;
    }

    public AdminLoginPageAssert passwordFieldHasValue(String expected) {
        adminLoginPage.passwordInput().shouldHave(value(expected));
        return this;
    }

    public AdminLoginPageAssert errorMessageIsVisible() {
        adminLoginPage.errorAlert().shouldBe(visible);
        return this;
    }

    public AdminLoginPageAssert errorMessageHasText(String expected) {
        adminLoginPage.errorAlert().shouldBe(visible).shouldHave(text(expected));
        return this;
    }
}
