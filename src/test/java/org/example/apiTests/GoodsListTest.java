package org.example.apiTests;

import io.qameta.allure.Step;
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
    @BeforeAll
    static void clearAllGoods() {
        deleteAllProducts();
    }

    @Step("Отправить GET /goods/list, page={page}, size={size}")
    private Response getGoodsList(int page, int size) {
        return given()
                .spec(rs)
                .queryParam("page", page)
                .queryParam("size", size)
                .log().all()
                .when()
                .get("/goods/list")
                .then()
                .log().all()
                .extract()
                .response();
    }

    @Step("Проверить, что код ответа равен {expectedStatus}")
    private void assertStatusCode(Response response, int expectedStatus) {
        assertThat(response.statusCode()).isEqualTo(expectedStatus);
    }

    @Step("Проверить, что товар {product} присутствует в списке с корректными данными")
    private void assertProductInList(Response response, ProductData product) {
        List<Integer> ids = response.jsonPath().getList("goods.id");
        List<String> names = response.jsonPath().getList("goods.name");
        List<Double> prices = response.jsonPath().getList("goods.price", Double.class);

        int index = ids.indexOf(product.id());

        assertThat(index)
                .as("Продукт с id %s должен быть в списке", product.id())
                .isNotEqualTo(-1);

        assertThat(names.get(index))
                .as("Ожидаем что название продукта %s, а получили %s", product.name(), names.get(index))
                .isEqualTo(product.name());

        assertThat(prices.get(index))
                .as("Ожидаем что цена продукта %s, а получили %s", product.price(), prices.get(index))
                .isEqualTo(product.price());
    }

    @Step("Проверить, что товар {product} отсутствует в списке")
    private void assertProductNotInList(Response response, ProductData product) {
        List<Integer> ids = response.jsonPath().getList("goods.id");
        assertThat(ids.contains(product.id()))
                .as("Продукт %s не должен быть в списке", product.name())
                .isFalse();
    }

    @Test
    @DisplayName("GET /goods/list -> 200, в ответе есть все созданные продукты")
    void listProducts_returns200_containsAllCreatedProductsWithCorrectData() {
        ProductData firstProduct = createProduct();
        ProductData secondProduct = createProduct();
        ProductData thirdProduct = createProduct();
        Response response = getGoodsList(0, 1000);

        assertStatusCode(response, 200);
        for (ProductData product : List.of(firstProduct, secondProduct, thirdProduct)) {
            assertProductInList(response, product);
        }
        deleteProduct(firstProduct.id());
        deleteProduct(secondProduct.id());
        deleteProduct(thirdProduct.id());
    }

    @Test
    @DisplayName("GET /goods/list -> 200 с дефолтными параметрами пагинации")
    void listProducts_returns200_withDefaults() {
        ProductData product = createProduct();
        Response response = getGoodsList(0, 1000);
        assertStatusCode(response, 200);
        deleteProduct(product.id());
    }

    @Test
    @DisplayName("GET /goods/list -> пагинация работает правильно")
    void listProducts_pageSizeIsRespected() {
        ProductData firstProduct = createProduct();
        ProductData secondProduct = createProduct();
        ProductData thirdProduct = createProduct();
        Response page0 = getGoodsList(0, 2);

        assertStatusCode(page0, 200);
        assertProductInList(page0, firstProduct);
        assertProductInList(page0, secondProduct);
        assertProductNotInList(page0, thirdProduct);

        deleteProduct(firstProduct.id());
        deleteProduct(secondProduct.id());
        deleteProduct(thirdProduct.id());
    }
}
