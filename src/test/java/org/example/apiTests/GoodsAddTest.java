package org.example.apiTests;

import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.example.apiTests.SharedGoodsData.*;

@Tag("Theme4.2")
@Order(1)
class GoodsAddTest extends BaseTest {

    @Test
    @DisplayName("POST /goods/add -> 200, когда запрос валидный")
    void addProduct_returns200_onValidPayload() {
        productName = randomName();

        Response response = given()
                .spec(rs)
                .body(new SharedGoodsData.ProductRequest(productName, randomPrice()))
                .when()
                .post("/goods/add");

        assertThat(response.statusCode())
                .as("Ожидаем статус 200")
                .isEqualTo(200);

        var data = response.jsonPath().getMap("data");
        assertThat(data)
                .as("Ответ не пустой и содержит один объект")
                .isNotNull()
                .hasSize(1);

        SharedGoodsData.productId = response.jsonPath().getLong("data.values()[0]");
        SharedGoodsData.registerCreatedId(SharedGoodsData.productId);
    }

    @Test
    @DisplayName("POST /goods/add -> 400, когда отсутствует имя")
    void addProduct_returns400_whenNameMissing() {
        Response response = given()
                .spec(rs)
                .body(Map.of("price", 9.99))
                .when()
                .post("/goods/add");

        assertThat(response.statusCode())
                .as("Ожидаем ошибку из за отсутствия name")
                .isEqualTo(400);
    }
}
