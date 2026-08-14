package org.example.apiTests;

import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.example.apiTests.SharedGoodsData.productName;

@Tag("Theme4.2")
@Order(2)
class GoodsGetTest extends BaseTest {

    @Test
    @DisplayName("GET /goods/{id} -> 200, успешно возращается ранее созданный продукт")
    void getProduct_returns200_whenExists() {
        Response response = given()
                .spec(rs)
                .when()
                .get("/goods/{id}", SharedGoodsData.productId);

        String actualName = response.jsonPath().getString("name");
        assertThat(response.statusCode())
                .as("Ожидаем успех и статус 200")
                .isEqualTo(200);
        assertThat(actualName)
                .as("Ожидаем название продукта: %a", productName)
                .isEqualTo(productName);
    }

    @Test
    @DisplayName("GET /goods/{id} -> 404, если продукта с таким id не существует")
    void getProduct_returns404_whenMissing() {
        Response response = given()
                .spec(rs)
                .when()
                .get("/goods/{id}", 999_999_999L);

        assertThat(response.statusCode())
                .as("Ожидаем что такого продукта не существует, статус должен быть 404")
                .isEqualTo(404);
    }
}