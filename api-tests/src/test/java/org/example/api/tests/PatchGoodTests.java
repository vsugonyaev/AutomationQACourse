package org.example.api.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.example.api.goods.model.ProductData;
import org.example.api.goods.model.ProductRequest;
import org.example.common.TestTags;
import org.example.common.config.TestConfig;
import org.example.common.data.TestData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.example.api.ApiAssert.assertThat;

@Epic("API")
@Feature("PATCH /goods/{id}")
class PatchGoodTests extends BaseApiTest {

    @Test
    @Tag(TestTags.SMOKE)
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("PATCH /goods/{id} -> 200: название товара меняется")
    void patchGood_newName_updatesName() {
        ProductData product = givenProduct();
        String newName = TestData.productName();

        assertThat(goodsApi.patchGood(product.id(), new ProductRequest(newName, product.price())))
                .statusCode(SC_OK)
                .productHas(newName, product.price());
        assertThat(goodsApi.getGood(product.id()))
                .statusCode(SC_OK)
                .productEquals(new ProductData(product.id(), newName, product.price()));
    }

    @Test
    @DisplayName("PATCH /goods/{id} -> 200: цена товара меняется")
    void patchGood_newPrice_updatesPrice() {
        ProductData product = givenProduct();
        double newPrice = TestData.price();

        assertThat(goodsApi.patchGood(product.id(), new ProductRequest(product.name(), newPrice)))
                .statusCode(SC_OK)
                .productHas(product.name(), newPrice);
    }

    @Test
    @DisplayName("PATCH /goods/{id} -> 400, когда новое название уже занято другим товаром")
    void patchGood_duplicateName_returns400() {
        ProductData first = givenProduct();
        ProductData second = givenProduct();

        assertThat(goodsApi.patchGood(second.id(), new ProductRequest(first.name(), second.price())))
                .statusCode(SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("PATCH /goods/{id} -> 404, если товара с таким id не существует")
    void patchGood_missing_returns404() {
        assertThat(goodsApi.patchGood(TestConfig.missingProductId(), randomProductRequest()))
                .statusCode(SC_NOT_FOUND);
    }

    @Test
    @DisplayName("PATCH /goods/{id} -> 401 без авторизации, товар не меняется")
    void patchGood_withoutAuth_returns401() {
        ProductData product = givenProduct();

        assertThat(anonymousGoodsApi.patchGood(product.id(), randomProductRequest()))
                .statusCode(SC_UNAUTHORIZED);
        assertThat(goodsApi.getGood(product.id()))
                .statusCode(SC_OK)
                .productEquals(product);
    }
}
