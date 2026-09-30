package org.example.pageobjects;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.example.config.ConfigReader;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.$x;

public class AdminPage {

    private final SelenideElement newNameInput = $("#n-name");
    private final SelenideElement newPriceInput = $("#n-price");
    private final SelenideElement addButton = $("#add-btn");
    private final ElementsCollection toasts = $$("#toast-container .toast");

    public AdminPage open() {
        Selenide.open(ConfigReader.getStandUrl() + "admin");
        return this;
    }

    public AdminPage enterNewProductName(String name) {
        newNameInput.shouldBe(Condition.visible).sendKeys(name);
        return this;
    }

    public AdminPage enterNewProductPrice(double price) {
        newPriceInput.sendKeys(String.valueOf(price));
        return this;
    }

    public AdminPage clickAddButton() {
        addButton.shouldBe(Condition.clickable).click();
        return this;
    }

    public AdminPage addProduct(String name, double price) {
        enterNewProductName(name);
        enterNewProductPrice(price);
        return clickAddButton();
    }

    public AdminPage setProductName(String currentRowName, String newName) {
        editNameInput(currentRowName).setValue(newName);
        return this;
    }

    public AdminPage setProductPrice(String currentRowName, double newPrice) {
        editPriceInput(currentRowName).setValue(String.valueOf(newPrice));
        return this;
    }

    public AdminPage clickUpdate(String currentRowName) {
        updateButton(currentRowName).shouldBe(Condition.clickable).click();
        return this;
    }

    public AdminPage clickDelete(String currentRowName) {
        deleteButton(currentRowName).shouldBe(Condition.clickable).click();
        return this;
    }

    public SelenideElement productRow(String name) {
        return $x(String.format("//tr[td/input[@value='%s']]", name));
    }

    private SelenideElement editNameInput(String name) {
        return $x(String.format("//tr[td/input[@value='%s']]//input[contains(@id, 'nm-')]", name));
    }

    private SelenideElement editPriceInput(String name) {
        return $x(String.format("//tr[td/input[@value='%s']]//input[@type='number']", name));
    }

    private SelenideElement updateButton(String name) {
        return $x(String.format("//tr[td/input[@value='%s']]//button[contains(@class, 'btn-upd')]", name));
    }

    private SelenideElement deleteButton(String name) {
        return $x(String.format("//tr[td/input[@value='%s']]//button[contains(@class, 'btn-del')]", name));
    }

    public SelenideElement newNameInput() {
        return newNameInput;
    }

    public SelenideElement newPriceInput() {
        return newPriceInput;
    }

    public SelenideElement addButton() {
        return addButton;
    }

    public ElementsCollection toasts() {
        return toasts;
    }

    public SelenideElement lastToast() {
        return toasts.last();
    }
}
