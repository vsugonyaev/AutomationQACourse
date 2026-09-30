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
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.example.api.ApiAssert.assertThat;

@Epic("API")
@Feature("DELETE /goods/{id}")
class DeleteGoodTests extends BaseApiTest {

    @Test
    @Tag(TestTags.SMOKE)
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("DELETE /goods/{id} -> 200: товар удаляется и больше не находится")
    void deleteGood_existing_removesProduct() {
        ProductData product = givenProduct();

        assertThat(goodsApi.deleteGood(product.id())).statusCode(SC_OK);
        assertThat(goodsApi.getGood(product.id())).statusCode(SC_NOT_FOUND);
    }

    @Test
    @DisplayName("DELETE /goods/{id} -> 404 для уже удалённого товара")
    void deleteGood_alreadyDeleted_returns404() {
        long deletedId = goodsSteps.deletedProductId();

        assertThat(goodsApi.deleteGood(deletedId))
                .statusCode(SC_NOT_FOUND)
                .bodyContains(TestConfig.apiNotFoundMessage(deletedId));
    }

    @Test
    @DisplayName("DELETE /goods/{id} -> 401 без авторизации, товар остаётся")
    void deleteGood_withoutAuth_returns401() {
        ProductData product = givenProduct();

        assertThat(anonymousGoodsApi.deleteGood(product.id())).statusCode(SC_UNAUTHORIZED);
        assertThat(goodsApi.getGood(product.id()))
                .statusCode(SC_OK)
                .productEquals(product);
    }
}
