package org.example.pageobjects;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.example.config.ConfigReader;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.$x;

public class AdminPage {

    private final SelenideElement newNameInput = $("#n-name");
    private final SelenideElement newPriceInput = $("#n-price");
    private final SelenideElement addButton = $("#add-btn");
    private final ElementsCollection toasts = $$("#toast-container .toast");

    @Step("Открыть страницу админки")
    public AdminPage open() {
        Selenide.open(ConfigReader.getStandUrl() + "admin");
        return this;
    }

    @Step("Ввести название нового товара: {name}")
    public AdminPage enterNewProductName(String name) {
        newNameInput.shouldBe(Condition.visible).sendKeys(name);
        return this;
    }

    @Step("Ввести цену нового товара: {price}")
    public AdminPage enterNewProductPrice(double price) {
        newPriceInput.sendKeys(String.valueOf(price));
        return this;
    }

    @Step("Нажать кнопку добавления товара")
    public AdminPage clickAddButton() {
        addButton.shouldBe(Condition.clickable).click();
        return this;
    }

    @Step("Добавить товар {name} ценой {price} через форму админки")
    public AdminPage addProduct(String name, double price) {
        enterNewProductName(name);
        enterNewProductPrice(price);
        return clickAddButton();
    }

    @Step("Изменить название товара {currentRowName} на {newName}")
    public AdminPage setProductName(String currentRowName, String newName) {
        editNameInput(currentRowName).setValue(newName);
        return this;
    }

    @Step("Изменить цену товара {currentRowName} на {newPrice}")
    public AdminPage setProductPrice(String currentRowName, double newPrice) {
        editPriceInput(currentRowName).setValue(String.valueOf(newPrice));
        return this;
    }

    @Step("Нажать кнопку обновления товара {currentRowName}")
    public AdminPage clickUpdate(String currentRowName) {
        updateButton(currentRowName).shouldBe(Condition.clickable).click();
        return this;
    }

    @Step("Нажать кнопку удаления товара {currentRowName}")
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
