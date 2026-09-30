package org.example.pageobjects;

import io.qameta.allure.Step;

public class ShopSteps {

    @Step("Войти в админку под пользователем {username}")
    public AdminPage loginAsAdmin(String username, String password) {
        return new AdminLoginPage().open().login(username, password);
    }

    @Step("Открыть главную страницу магазина")
    public MainPage openShop() {
        return new MainPage().open();
    }

    @Step("Оплатить текущую корзину")
    public MainPage payCart(MainPage mainPage) {
        mainPage.clickOpenCart();
        mainPage.clickMakeOrder();
        return mainPage;
    }

    @Step("Переименовать товар {currentName} в {newName} и сохранить изменения")
    public AdminPage renameProduct(AdminPage adminPage, String currentName, String newName) {
        adminPage.setProductName(currentName, newName);
        return adminPage.clickUpdate(currentName);
    }

    @Step("Изменить цену товара {productName} на {newPrice} и сохранить изменения")
    public AdminPage changeProductPrice(AdminPage adminPage, String productName, double newPrice) {
        adminPage.setProductPrice(productName, newPrice);
        return adminPage.clickUpdate(productName);
    }
}
