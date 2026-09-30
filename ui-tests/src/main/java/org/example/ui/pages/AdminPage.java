package org.example.ui.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.example.common.config.TestConfig;
import org.example.ui.util.PriceFormat;

import static com.codeborne.selenide.Condition.clickable;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/** Админка после входа: форма добавления товара и таблица товаров. */
public class AdminPage {

    private static final String NAME_INPUT = "#nm-%d";
    private static final String PRICE_INPUT = "#pr-%d";
    private static final String SAVE_BUTTON = "button.btn-upd[data-id='%d']";

    private final SelenideElement newNameInput = $("#n-name");
    private final SelenideElement newPriceInput = $("#n-price");
    private final SelenideElement addButton = $("#add-btn");
    private final ElementsCollection productNameInputs = $$("#tbody input[id^='nm-']");
    private final ElementsCollection toasts = $$("#toast-container .toast");

    @Step("Открыть админку")
    public AdminPage open() {
        Selenide.open(TestConfig.adminPath());
        return this;
    }

    @Step("Ввести название нового товара: '{name}'")
    public AdminPage enterNewProductName(String name) {
        newNameInput.shouldBe(visible).setValue(name);
        return this;
    }

    @Step("Ввести цену нового товара: {price}")
    public AdminPage enterNewProductPrice(double price) {
        newPriceInput.shouldBe(visible).setValue(PriceFormat.asShown(price));
        return this;
    }

    @Step("Нажать кнопку добавления товара")
    public AdminPage clickAdd() {
        addButton.shouldBe(clickable).click();
        return this;
    }

    @Step("Добавить товар '{name}' с ценой {price}")
    public AdminPage addProduct(String name, double price) {
        enterNewProductName(name);
        enterNewProductPrice(price);
        return clickAdd();
    }

    @Step("Изменить название товара #{id} на '{newName}'")
    public AdminPage setProductName(long id, String newName) {
        productNameInput(id).shouldBe(visible).setValue(newName);
        return this;
    }

    @Step("Изменить цену товара #{id} на {newPrice}")
    public AdminPage setProductPrice(long id, double newPrice) {
        $(String.format(PRICE_INPUT, id)).shouldBe(visible).setValue(PriceFormat.asShown(newPrice));
        return this;
    }

    @Step("Нажать 'Сохранить' у товара #{id}")
    public AdminPage clickSave(long id) {
        $(String.format(SAVE_BUTTON, id)).shouldBe(clickable).click();
        return this;
    }

    public SelenideElement productNameInput(long id) {
        return $(String.format(NAME_INPUT, id));
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

    public ElementsCollection productNameInputs() {
        return productNameInputs;
    }

    public ElementsCollection toasts() {
        return toasts;
    }
}
