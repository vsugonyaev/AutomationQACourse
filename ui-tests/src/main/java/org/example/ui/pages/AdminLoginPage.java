package org.example.ui.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.example.common.config.TestConfig;

import static com.codeborne.selenide.Condition.clickable;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

/** Форма входа в админку: логин, пароль, кнопка «Войти». */
public class AdminLoginPage {

    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement signInButton = $("button.primary");
    private final SelenideElement errorAlert = $("div.alert.alert-danger");

    @Step("Открыть админку (перенаправит на форму входа)")
    public AdminLoginPage open() {
        Selenide.open(TestConfig.adminPath());
        return this;
    }

    @Step("Ввести логин '{username}'")
    public AdminLoginPage enterUsername(String username) {
        usernameInput.shouldBe(visible).setValue(username);
        return this;
    }

    @Step("Ввести пароль")
    public AdminLoginPage enterPassword(String password) {
        passwordInput.shouldBe(visible).setValue(password);
        return this;
    }

    @Step("Нажать кнопку 'Войти'")
    public AdminLoginPage submit() {
        signInButton.shouldBe(clickable).click();
        return this;
    }

    @Step("Войти в админку под пользователем '{username}'")
    public AdminPage loginAs(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        submit();
        return new AdminPage();
    }

    public SelenideElement usernameInput() {
        return usernameInput;
    }

    public SelenideElement passwordInput() {
        return passwordInput;
    }

    public SelenideElement signInButton() {
        return signInButton;
    }

    public SelenideElement errorAlert() {
        return errorAlert;
    }
}
