package org.example.ui.tests;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.example.api.goods.GoodsSteps;
import org.example.api.goods.model.ProductData;
import org.example.common.data.TestData;
import org.example.ui.config.BrowserSetup;
import org.example.ui.steps.ShopSteps;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriverException;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public abstract class BaseUiTest {

    private static final String SCREENSHOT_NAME = "Скриншот в конце теста";
    private static final String PNG_TYPE = "image/png";
    private static final String PNG_EXTENSION = "png";

    protected final ShopSteps shopSteps = new ShopSteps();
    protected final GoodsSteps goodsSteps = GoodsSteps.asAdmin();

    private final List<Long> createdIds = new ArrayList<>();
    private final List<String> namesCreatedInUi = new ArrayList<>();

    @BeforeAll
    static void configureBrowser() {
        BrowserSetup.configure();
    }

    @AfterEach
    void tearDown() {
        attachScreenshot();
        Selenide.closeWebDriver();
        namesCreatedInUi.forEach(name -> createdIds.addAll(goodsSteps.findIdsByName(name)));
        createdIds.forEach(goodsSteps::deleteQuietly);
    }

    protected ProductData givenProduct() {
        return givenProduct(TestData.price());
    }

    protected ProductData givenProduct(double price) {
        ProductData product = goodsSteps.createProduct(TestData.productName(), price);
        createdIds.add(product.id());
        return product;
    }

    @Step("Подготовить {count} товаров с ценой от {minPrice} до {maxPrice}")
    protected List<ProductData> givenProducts(int count, double minPrice, double maxPrice) {
        return Stream.generate(() -> givenProduct(TestData.priceBetween(minPrice, maxPrice)))
                .limit(count)
                .toList();
    }

    /** Товар создаётся через UI самим тестом — регистрируем имя заранее, чтобы удалить его после теста. */
    protected void cleanUpAfterTest(String productName) {
        namesCreatedInUi.add(productName);
    }

    protected static List<String> namesOf(List<ProductData> products) {
        return products.stream().map(ProductData::name).toList();
    }

    // Plain sequential sum on purpose: the storefront JS adds prices one by one in cart order.
    protected static double sumOfPrices(List<ProductData> products) {
        double total = 0;
        for (ProductData product : products) {
            total += product.price();
        }
        return total;
    }

    private void attachScreenshot() {
        if (!WebDriverRunner.hasWebDriverStarted()) {
            return;
        }
        try {
            byte[] png = Selenide.screenshot(OutputType.BYTES);
            if (png != null) {
                Allure.addAttachment(SCREENSHOT_NAME, PNG_TYPE, new ByteArrayInputStream(png), PNG_EXTENSION);
            }
        } catch (WebDriverException e) {
            // An open JS alert blocks screenshots; the test result itself is already recorded.
        }
    }
}
