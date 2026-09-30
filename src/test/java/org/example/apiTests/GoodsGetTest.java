package org.example.apiTests;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.example.apiTests.SharedGoodsData.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(3)
@Tag("Theme4.2")
class GoodsGetTest extends BaseTest {

    @Step("Отправить GET /goods/{id}")
    private Response getGood(int id) {
        return given()
                .spec(rs)
                .log().all()
                .when()
                .get("/goods/{id}", id)
                .then()
                .log().all()
                .extract()
                .response();
    }

    @Step("Проверить, что код ответа равен {expectedStatus}")
    private void assertStatusCode(Response response, int expectedStatus, String message) {
        assertThat(response.statusCode())
                .as(message)
                .isEqualTo(expectedStatus);
    }

    @Step("Проверить, что тело ответа не пустое")
    private void assertBodyNotEmpty(Response response) {
        assertThat(response.body())
                .as("Ответ не пустой и содержит один объект")
                .isNotNull();
    }

    @Step("Проверить название и цену товара в ответе")
    private void assertProductData(Response response, String expectedName, double expectedPrice) {
        String actualName = response.jsonPath().getString("name");
        assertThat(actualName)
                .as("Ожидаем увидеть название %s, а получили %s", expectedName, actualName)
                .isEqualTo(expectedName);

        double actualPrice = response.jsonPath().getDouble("price");
        assertThat(actualPrice)
                .as("Ожидаем увидеть цену %s, а получили %s", expectedPrice, actualPrice)
                .isEqualTo(expectedPrice);
    }

    @Test
    @DisplayName("GET /goods/{id} -> 200, успешно возращается ранее созданный продукт")
    void getProduct_returns200_whenExists() {
        ProductData product = createProduct();
        Response response = getGood(product.id());

        assertStatusCode(response, 200, "Ожидаем статус 200");
        assertBodyNotEmpty(response);
        assertProductData(response, product.name(), product.price());

        int productId = response.jsonPath().getInt("id");
        deleteProduct(productId);
    }

    @Test
    @DisplayName("GET /goods/{id} -> 404, если продукта с таким id не существует")
    void getProduct_returns404_whenMissing() {
        Response response = getGood(999);

        assertStatusCode(response, 404, "Ожидаем что такого продукта не существует, статус должен быть 404");
    }
}
