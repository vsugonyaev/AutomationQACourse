package org.example.pageobjects;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.example.config.ConfigReader;

import static com.codeborne.selenide.Selenide.$;

public class AdminLoginPage {

    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement loginButton = $("button.primary");
    private final SelenideElement errorAlert = $("div.alert.alert-danger");

    public AdminLoginPage open() {
        Selenide.open(ConfigReader.getStandUrl() + "admin");
        return this;
    }

    public AdminLoginPage enterUsername(String username) {
        usernameInput.shouldBe(Condition.visible).sendKeys(username);
        return this;
    }

    public AdminLoginPage enterPassword(String password) {
        passwordInput.shouldBe(Condition.visible).sendKeys(password);
        return this;
    }

    public AdminPage clickLoginButton() {
        loginButton.shouldBe(Condition.clickable).click();
        return new AdminPage();
    }

    public AdminPage login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        return clickLoginButton();
    }

    public String getErrorMessageText() {
        return errorAlert.shouldBe(Condition.visible).getText();
    }

    public SelenideElement usernameInput() {
        return usernameInput;
    }

    public SelenideElement passwordInput() {
        return passwordInput;
    }

    public SelenideElement loginButton() {
        return loginButton;
    }

    public SelenideElement errorAlert() {
        return errorAlert;
    }
}
