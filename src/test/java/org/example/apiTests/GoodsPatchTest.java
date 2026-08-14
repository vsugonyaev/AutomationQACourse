package org.example.apiTests;

import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.example.apiTests.SharedGoodsData.*;

@Tag("Theme4.2")
@Order(3)
class GoodsPatchTest extends BaseTest {

    @Test
    @DisplayName("PATCH /goods/{id} -> 200, удачное переименование продукта")
    void patchProduct_returns200_onValidUpdate() {
        productName = randomName();
        Response response = given()
                .spec(rs)
                .body(new ProductRequest(productName, randomPrice()))
                .when()
                .patch("/goods/{id}", SharedGoodsData.productId);

        assertThat(response.statusCode()).isEqualTo(200);

        Response getResponse = given()
                .spec(rs)
                .when()
                .get("/goods/{id}", SharedGoodsData.productId);
        String actualName = getResponse.jsonPath().getString("name");
        assertThat(actualName)
                .as("Ожидаем название продукта: %a", productName)
                .isEqualTo(SharedGoodsData.productName);
    }

    @Test
    @DisplayName("PATCH /goods/{id} -> 400, когда пытаемся установить уже существуещее название продукта")
    void patchProduct_returns400_onDuplicateName() {
        String otherName = randomName();

        Response createResponse = given()
                .spec(rs)
                .body(new ProductRequest(otherName, randomPrice()))
                .when()
                .post("/goods/add");

        assertThat(createResponse.statusCode())
                .isEqualTo(200);
        long otherId = createResponse.jsonPath().getInt("data.values()[0]");
        SharedGoodsData.registerCreatedId(otherId);

        Response patchResponse = given()
                .spec(rs)
                .body(new ProductRequest(productName,randomPrice()))
                .when()
                .patch("/goods/{id}", otherId);

        assertThat(patchResponse.statusCode())
                .as("Ожидаем ошибку, статус 400")
                .isEqualTo(400);
        assertThat(patchResponse.jsonPath().getString("message"))
                .isNotBlank();
    }

    @Test
    @DisplayName("PATCH /goods/{id} -> 404, если продукта с таким id не существует")
    void patchProduct_returns404_whenMissing() {
        Response response = given()
                .spec(rs)
                .body(new ProductRequest(randomName(),randomPrice()))
                .when()
                .patch("/goods/{id}", 999_999_997L);

        assertThat(response.statusCode())
                .as("Ожидаем что такого продукта не существует, статус 404")
                .isEqualTo(404);
    }
}
