package org.example.api.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.example.api.goods.model.AddGoodResult;
import org.example.api.goods.model.ProductData;
import org.example.api.goods.model.ProductRequest;
import org.example.common.TestTags;
import org.example.common.config.TestConfig;
import org.example.common.data.TestData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.example.api.ApiAssert.assertThat;

@Epic("API")
@Feature("POST /goods/add")
class AddGoodTests extends BaseApiTest {

    private static final String PRICE_FIELD = "price";

    @Test
    @Tag(TestTags.SMOKE)
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("POST /goods/add -> 200: товар создаётся и сохраняется с переданными данными")
    void addGood_validPayload_createsProduct() {
        ProductRequest request = randomProductRequest();

        Response response = goodsApi.addGood(request);

        assertThat(response)
                .statusCode(SC_OK)
                .createdWithMessage(TestConfig.apiAddSuccessMessage());
        long id = response.as(AddGoodResult.class).createdId();
        trackForCleanup(id);
        assertThat(goodsApi.getGood(id))
                .statusCode(SC_OK)
                .productEquals(new ProductData(id, request.name(), request.price()));
    }

    @Test
    @DisplayName("POST /goods/add -> 400, когда не передано название")
    void addGood_withoutName_returns400() {
        Response response = goodsApi.addGood(ProductRequest.withoutName(TestData.price()));

        assertThat(response).statusCode(SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("POST /goods/add -> 400, когда товар с таким названием уже существует")
    void addGood_duplicateName_returns400() {
        ProductData existing = givenProduct();

        Response response = goodsApi.addGood(new ProductRequest(existing.name(), TestData.price()));

        assertThat(response).statusCode(SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("POST /goods/add -> 400, когда цена отрицательная")
    void addGood_negativePrice_returns400() {
        Response response = goodsApi.addGood(new ProductRequest(TestData.productName(), -TestData.price()));

        assertThat(response)
                .statusCode(SC_BAD_REQUEST)
                .validationError(PRICE_FIELD, TestConfig.apiNegativePriceMessage());
    }

    @Test
    @DisplayName("POST /goods/add -> 401 без авторизации")
    void addGood_withoutAuth_returns401() {
        Response response = anonymousGoodsApi.addGood(randomProductRequest());

        assertThat(response).statusCode(SC_UNAUTHORIZED);
    }
}
