package org.example.ui.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.example.api.goods.model.ProductData;
import org.example.common.TestTags;
import org.example.common.config.TestConfig;
import org.example.common.data.TestData;
import org.example.ui.asserts.AdminLoginPageAssert;
import org.example.ui.asserts.AdminPageAssert;
import org.example.ui.asserts.MainPageAssert;
import org.example.ui.pages.AdminLoginPage;
import org.example.ui.pages.AdminPage;
import org.example.ui.pages.MainPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Epic("UI")
@Feature("Админка")
class AdminPanelTests extends BaseUiTest {

    @Test
    @Tag(TestTags.SMOKE)
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("2.1. Добавить товар через админку, выйти на витрину и проверить, что товар отображается")
    void productAddedInAdminIsShownOnStorefront() {
        String name = TestData.productName();
        double price = TestData.price();
        cleanUpAfterTest(name);

        AdminPage adminPage = shopSteps.loginAsAdmin();
        adminPage.addProduct(name, price);
        new AdminPageAssert(adminPage).toastIsShown(TestConfig.productAddedMessage());

        MainPage mainPage = shopSteps.openStorefront();
        new MainPageAssert(mainPage)
                .productCardIsVisible(name)
                .productCardHasPrice(name, price);
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("2.3. Попытаться войти в админку с неверным логином и паролем")
    void loginWithInvalidCredentialsIsRejected() {
        AdminLoginPage loginPage = new AdminLoginPage().open();
        AdminLoginPageAssert loginAssert = new AdminLoginPageAssert(loginPage);

        shopSteps.tryLogin(loginPage, TestData.differentFrom(TestConfig.adminUsername()), TestConfig.adminPassword());
        loginAssert.errorMessageIs(TestConfig.loginErrorMessage()).loginFormIsVisible();

        shopSteps.tryLogin(loginPage, TestConfig.adminUsername(), TestData.differentFrom(TestConfig.adminPassword()));
        loginAssert.errorMessageIs(TestConfig.loginErrorMessage()).loginFormIsVisible();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("3.3. Войти в админку и добавить товар. Проверить уведомление после добавления товара.")
    void addingProductInAdminShowsNotification() {
        String name = TestData.productName();
        double price = TestData.price();
        cleanUpAfterTest(name);

        AdminPage adminPage = shopSteps.loginAsAdmin();
        new AdminPageAssert(adminPage).addFormIsVisible();
        adminPage.addProduct(name, price);

        new AdminPageAssert(adminPage)
                .toastIsShown(TestConfig.productAddedMessage())
                .productIsListed(name);
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("3.4. Войти в админку и отредактировать товар. Выйти на список товаров и проверить, что изменения применились.")
    void editedProductIsUpdatedOnStorefront() {
        ProductData product = givenProduct();
        String newName = TestData.productName();
        double newPrice = TestData.price();

        AdminPage adminPage = shopSteps.loginAsAdmin();
        AdminPageAssert adminPageAssert = new AdminPageAssert(adminPage);
        shopSteps.renameProduct(adminPage, product.id(), newName);
        adminPageAssert.toastIsShown(TestConfig.productUpdatedMessage(product.id()));

        MainPage mainPage = shopSteps.openStorefront();
        MainPageAssert mainPageAssert = new MainPageAssert(mainPage)
                .productCardIsVisible(newName)
                .productCardHasPrice(newName, product.price());

        adminPage.open();
        shopSteps.changeProductPrice(adminPage, product.id(), newPrice);
        adminPageAssert.toastIsShown(TestConfig.productUpdatedMessage(product.id()));

        mainPage.open();
        mainPageAssert.productCardHasPrice(newName, newPrice);
    }
}
