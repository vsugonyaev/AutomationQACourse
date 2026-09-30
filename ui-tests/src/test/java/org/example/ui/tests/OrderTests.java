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
@Feature("Оформление заказа")
class OrderTests extends BaseUiTest {

    private static final int ORDER_ITEMS = 3;
    private static final int EMPTY_CART = 0;
    private static final int ONE_ITEM = 1;
    // Keeps the order total clearly on one side of the limit, away from rounding at the boundary.
    private static final double LIMIT_MARGIN = 1.0;

    @Test
    @Tag(TestTags.SMOKE)
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("3.1. Добавить три единицы товара в корзину и оплатить их (общая стоимость не должна превышать 300 рублей). Проверить уведомление об обработке заказа.")
    void orderWithinLimitIsAccepted() {
        List<ProductData> products = givenProducts(ORDER_ITEMS,
                TestConfig.productPriceMin(), unitPriceAtLimit() - LIMIT_MARGIN);

        MainPage mainPage = shopSteps.openStorefront();
        shopSteps.addToCart(mainPage, namesOf(products));
        MainPageAssert mainPageAssert = new MainPageAssert(mainPage);
        products.forEach(product ->
                mainPageAssert.toastIsShown(TestConfig.addedToCartMessage(product.name(), ONE_ITEM)));
        mainPageAssert.cartCountIs(ORDER_ITEMS);

        shopSteps.checkout(mainPage);

        mainPageAssert
                .toastIsShown(TestConfig.orderAcceptedMessage())
                .mainTitleIs(TestConfig.orderPlacedTitle())
                .cartCountIs(EMPTY_CART);
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("2.5. Добавить в корзину товаров более чем на 300 рублей и нажать на кнопку «Оформить заказ». Проверить, что отображается JS Alert.")
    void orderOverLimitShowsAlert() {
        List<ProductData> products = givenProducts(ORDER_ITEMS,
                unitPriceAtLimit() + LIMIT_MARGIN, unitPriceAtLimit() * 2);

        MainPage mainPage = shopSteps.openStorefront();
        shopSteps.addToCart(mainPage, namesOf(products));
        new MainPageAssert(mainPage).cartCountIs(ORDER_ITEMS);

        shopSteps.checkout(mainPage);

        // The storefront reports the total truncated to whole roubles (JS parseInt).
        int shownTotal = (int) sumOfPrices(products);
        new MainPageAssert(mainPage).alertTextIs(TestConfig.orderLimitExceededMessage(shownTotal));
        mainPage.acceptAlert();
    }

    private static double unitPriceAtLimit() {
        return (double) TestConfig.orderLimit() / ORDER_ITEMS;
    }
}
