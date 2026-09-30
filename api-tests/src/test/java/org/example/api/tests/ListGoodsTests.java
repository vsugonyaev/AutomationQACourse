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

import java.util.List;
import java.util.stream.Stream;

import static org.apache.http.HttpStatus.SC_OK;
import static org.example.api.ApiAssert.assertThat;

@Epic("API")
@Feature("GET /goods/list")
class ListGoodsTests extends BaseApiTest {

    private static final int FIRST_PAGE = 0;
    private static final int PAGE_SIZE = 2;
    // One product more than a page holds, so pagination has something to cut.
    private static final int PRODUCTS_TO_CREATE = PAGE_SIZE + 1;

    @Test
    @Tag(TestTags.SMOKE)
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("GET /goods/list -> 200: в списке есть все созданные товары с корректными данными")
    void listGoods_containsCreatedProducts() {
        List<ProductData> products = givenProducts();

        var listAssert = assertThat(goodsApi.listGoods(FIRST_PAGE, TestConfig.goodsListMaxSize()))
                .statusCode(SC_OK);
        products.forEach(listAssert::listContains);
    }

    @Test
    @DisplayName("GET /goods/list -> 200 без параметров пагинации")
    void listGoods_withoutParams_returnsList() {
        givenProduct();

        assertThat(goodsApi.listGoods())
                .statusCode(SC_OK)
                .hasGoodsList();
    }

    @Test
    @DisplayName("GET /goods/list -> пагинация работает правильно")
    void listGoods_pageSizeIsRespected() {
        goodsSteps.deleteAllProducts();
        List<ProductData> products = givenProducts();
        ProductData first = products.get(0);
        ProductData second = products.get(1);
        ProductData third = products.get(2);

        assertThat(goodsApi.listGoods(FIRST_PAGE, PAGE_SIZE))
                .statusCode(SC_OK)
                .listContains(first)
                .listContains(second)
                .listDoesNotContain(third.id());
    }

    private List<ProductData> givenProducts() {
        return Stream.generate(this::givenProduct).limit(PRODUCTS_TO_CREATE).toList();
    }
}
