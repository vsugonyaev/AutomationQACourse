package org.example.ui.steps;

import io.qameta.allure.Step;
import org.example.common.config.TestConfig;
import org.example.ui.pages.AdminLoginPage;
import org.example.ui.pages.AdminPage;
import org.example.ui.pages.MainPage;

import java.util.List;

/** Бизнес-шаги UI-сценариев поверх PageObject-классов. */
public class ShopSteps {

    @Step("Войти в админку под администратором")
    public AdminPage loginAsAdmin() {
        return new AdminLoginPage().open().loginAs(TestConfig.adminUsername(), TestConfig.adminPassword());
    }

    @Step("Попытаться войти в админку с логином '{username}'")
    public AdminLoginPage tryLogin(AdminLoginPage loginPage, String username, String password) {
        return loginPage.enterUsername(username).enterPassword(password).submit();
    }

    @Step("Открыть витрину магазина")
    public MainPage openStorefront() {
        return new MainPage().open();
    }

    @Step("Добавить в корзину товары: {productNames}")
    public MainPage addToCart(MainPage mainPage, List<String> productNames) {
        productNames.forEach(mainPage::clickAddToCart);
        return mainPage;
    }

    @Step("Оформить заказ из корзины")
    public MainPage checkout(MainPage mainPage) {
        return mainPage.openCart().clickMakeOrder();
    }

    @Step("Переименовать товар #{id} в '{newName}' и сохранить")
    public AdminPage renameProduct(AdminPage adminPage, long id, String newName) {
        return adminPage.setProductName(id, newName).clickSave(id);
    }

    @Step("Изменить цену товара #{id} на {newPrice} и сохранить")
    public AdminPage changeProductPrice(AdminPage adminPage, long id, double newPrice) {
        return adminPage.setProductPrice(id, newPrice).clickSave(id);
    }
}
