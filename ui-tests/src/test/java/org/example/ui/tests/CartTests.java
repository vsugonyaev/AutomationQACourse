package org.example.ui.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.example.api.goods.model.ProductData;
import org.example.common.TestTags;
import org.example.common.config.TestConfig;
import org.example.ui.asserts.MainPageAssert;
import org.example.ui.pages.MainPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

@Epic("UI")
@Feature("Корзина")
class CartTests extends BaseUiTest {

    private static final int ONE_ITEM = 1;

    @Test
    @Tag(TestTags.SMOKE)
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("2.2. Добавить товар в корзину и проверить, что он отображается")
    void addedProductIsShownInCart() {
        ProductData product = givenProduct();

        MainPage mainPage = shopSteps.openStorefront();
        mainPage.clickAddToCart(product.name());

        MainPageAssert mainPageAssert = new MainPageAssert(mainPage)
                .toastIsShown(TestConfig.addedToCartMessage(product.name(), ONE_ITEM))
                .cartCountIs(ONE_ITEM);
        mainPage.openCart();
        mainPageAssert
                .cartContains(product.name())
                .cartItemsCountIs(ONE_ITEM);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("2.4. Проверить сохранение товаров в корзине после обновления страницы")
    void cartKeepsProductsAfterPageRefresh() {
        List<ProductData> products = List.of(givenProduct(), givenProduct());

        MainPage mainPage = shopSteps.openStorefront();
        shopSteps.addToCart(mainPage, namesOf(products));
        MainPageAssert mainPageAssert = new MainPageAssert(mainPage).cartCountIs(products.size());

        mainPage.refresh().openCart();
        products.forEach(product -> mainPageAssert.cartContains(product.name()));
        mainPageAssert.cartItemsCountIs(products.size());
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("3.2. Добавить в корзину несколько разных товаров и проверить, что общая цена в корзине считается корректно")
    void cartTotalIsSumOfProductPrices() {
        List<ProductData> products = List.of(
                givenProduct(TestConfig.cartFirstProductPrice()),
                givenProduct(TestConfig.cartSecondProductPrice()));
        String expectedTotal = String.format("%.0f", sumOfPrices(products));

        MainPage mainPage = shopSteps.openStorefront();
        MainPageAssert mainPageAssert = new MainPageAssert(mainPage);
        products.forEach(product -> {
            mainPage.clickAddToCart(product.name());
            mainPageAssert.toastIsShown(TestConfig.addedToCartMessage(product.name(), ONE_ITEM));
        });
        mainPageAssert.cartCountIs(products.size());

        mainPage.openCart();
        mainPageAssert.totalPriceHasText(expectedTotal);
    }
}
