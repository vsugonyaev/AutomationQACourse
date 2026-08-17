package org.example.apiTests;

import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.example.apiTests.SharedGoodsData.*;


@Tag("Theme4.2")
class GoodsGetTest extends BaseTest {

    @Test
    @DisplayName("GET /goods/{id} -> 200, успешно возращается ранее созданный продукт")
    void getProduct_returns200_whenExists() {
        ProductData product = createProduct();
        Response response = given()
                .spec(rs)
                .when()
                .get("/goods/{id}", product.id())
                .then()
                .log().all()
                .extract()
                .response();;

        assertThat(response.statusCode())
                .as("Ожидаем статус 200")
                .isEqualTo(200);
        var data = response.jsonPath().getMap("data");
        assertThat(data)
                .as("Ответ не пустой и содержит один объект")
                .isNotNull()
                .hasSize(1);

        String actualName = response.jsonPath().getString("data.name");
        assertThat(actualName)
                .as("Ожидаем увидеть название %s, а получили %d", product.name(), actualName)
                .isEqualTo(product.name());

        double actualPrice = response.jsonPath().getDouble("data.price");
        assertThat(actualPrice)
                .as("Ожидаем увидеть цену %s, а получили %d", product.price(), actualPrice)
                .isEqualTo(product.price());

        int productId = response.jsonPath().getInt("data.id");
        deleteProduct(productId);
    }

    @Test
    @DisplayName("GET /goods/{id} -> 404, если продукта с таким id не существует")
    void getProduct_returns404_whenMissing() {
        Response response = given()
                .spec(rs)
                .when()
                .get("/goods/{id}", 999)
                .then()
                .log().all()
                .extract()
                .response();;

        assertThat(response.statusCode())
                .as("Ожидаем что такого продукта не существует, статус должен быть 404")
                .isEqualTo(404);
    }
}