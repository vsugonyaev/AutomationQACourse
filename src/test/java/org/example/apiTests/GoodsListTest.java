package org.example.apiTests;

import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.example.apiTests.SharedGoodsData.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(5)
@Tag("Theme4.2")
class GoodsListTest extends BaseTest {

    @Test
    @DisplayName("GET /goods/list -> 200, в ответе есть все созданные продукты")
    void listProducts_returns200_containsAllCreatedProductsWithCorrectData() {
        ProductData firstProduct = createProduct();
        ProductData secondProduct = createProduct();
        ProductData thirdProduct = createProduct();
        Response response = given()
                .spec(rs)
                .queryParam("page", 0)
                .queryParam("size", 1000)
                .log().all()
                .when()
                .get("/goods/list")
                .then()
                .log().all()
                .extract()
                .response();

        assertThat(response.statusCode()).isEqualTo(200);
        List<Integer> ids = response.jsonPath().getList("goods.id");
        List<String> names = response.jsonPath().getList("goods.name");
        List<Double> prices = response.jsonPath().getList("goods.price");
        List<ProductData> products = List.of(firstProduct, secondProduct, thirdProduct);
        for (ProductData product : products) {

            int index = ids.indexOf(product.id());

            assertThat(index)
                    .as("Продукт с id %s должен быть в списке", product.id())
                    .isNotEqualTo(-1);

            assertThat(names.get(index))
                    .as("Ожидаем что название продукта %s, а получили %d", product.name(), names.get(index))
                    .isEqualTo(product.name());

            assertThat(prices.get(index))
                    .as("Ожидаем что цена продукта %s, а получили %d", product.price(), prices.get(index))
                    .isEqualTo(product.price());
        }
        deleteProduct(firstProduct.id());
        deleteProduct(secondProduct.id());
        deleteProduct(thirdProduct.id());
}

    @Test
    @DisplayName("GET /goods/list -> 200 с дефолтными параметрами пагинации")
    void listProducts_returns200_withDefaults() {
        ProductData product = createProduct();
        Response response = given()
                .spec(rs)
                .log().all()
                .when().get("/goods/list")
                .then()
                .log().all()
                .extract()
                .response();
        assertThat(response.statusCode())
                .isEqualTo(200);
        deleteProduct(product.id());
    }

    @Test
    @DisplayName("GET /goods/list -> пагинация работает правильно")
    void listProducts_pageSizeIsRespected() {
        ProductData firstProduct = createProduct();
        ProductData secondProduct = createProduct();
        ProductData thirdProduct = createProduct();
        Response page0 = given()
                .spec(rs)
                .queryParam("page", 0)
                .queryParam("size", 2)
                .log().all()
                .when()
                .get("/goods/list")
                .then()
                .log().all()
                .extract()
                .response();

        assertThat(page0.statusCode()).isEqualTo(200);
        List<Integer> ids = page0.jsonPath().getList("goods.id");
        List<String> names = page0.jsonPath().getList("goods.name");
        List<Double> prices = page0.jsonPath().getList("goods.price");

        int index1 = ids.indexOf(firstProduct.id());

        assertThat(index1)
                .as("Первый продукт должен быть в списке")
                .isNotEqualTo(-1);

        assertThat(names.get(index1))
                .as("Ожидаем что название продукта %s, а получили %d", firstProduct.name(), names.get(index1))
                .isEqualTo(firstProduct.name());

        assertThat(prices.get(index1))
                .as("Ожидаем что цена продукта %s, а получили %d", firstProduct.price(), prices.get(index1))
                .isEqualTo(firstProduct.price());

        int index2 = ids.indexOf(secondProduct.id());

        assertThat(index2)
                .as("Второй продукт должен быть в списке")
                .isNotEqualTo(-1);

        assertThat(names.get(index2))
                .as("Ожидаем что название продукта %s, а получили %d", secondProduct.name(), names.get(index2))
                .isEqualTo(secondProduct.name());

        assertThat(prices.get(index2))
                .as("Ожидаем что цена продукта %s, а получили %d", secondProduct.price(), prices.get(index2))
                .isEqualTo(secondProduct.price());

        assertThat(ids.contains(thirdProduct.id()))
                .as("Третий продукт не должен быть в списке")
                .isFalse();
        deleteProduct(firstProduct.id());
        deleteProduct(secondProduct.id());
        deleteProduct(thirdProduct.id());
    }
}
