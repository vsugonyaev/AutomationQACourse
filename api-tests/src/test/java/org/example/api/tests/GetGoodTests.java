package org.example.api.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.example.api.goods.model.ProductData;
import org.example.common.TestTags;
import org.example.common.config.TestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
import static org.example.api.ApiAssert.assertThat;

@Epic("API")
@Feature("GET /goods/{id}")
class GetGoodTests extends BaseApiTest {

    @Test
    @Tag(TestTags.SMOKE)
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("GET /goods/{id} -> 200: возвращается ранее созданный товар")
    void getGood_existing_returnsProduct() {
        ProductData product = givenProduct();

        assertThat(goodsApi.getGood(product.id()))
                .statusCode(SC_OK)
                .productEquals(product);
    }

    @Test
    @DisplayName("GET /goods/{id} -> 404, если товара с таким id не существует")
    void getGood_missing_returns404() {
        assertThat(goodsApi.getGood(TestConfig.missingProductId()))
                .statusCode(SC_NOT_FOUND);
    }
}
