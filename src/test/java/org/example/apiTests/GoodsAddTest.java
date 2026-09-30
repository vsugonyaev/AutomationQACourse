package org.example.apiTests;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.example.apiTests.SharedGoodsData.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(1)
@Tag("Theme4.2")
public class GoodsAddTest extends BaseTest {
   @BeforeAll
   static void clearAllGoods() {
       deleteAllProducts();
   }

    @Step("Отправить POST /goods/add с телом {body}")
    private Response addGood(Object body) {
        return given()
                .spec(rs)
                .body(body)
                .log().all()
                .when()
                .post("/goods/add")
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

    @Step("Проверить, что тело ответа содержит один объект data")
    private void assertDataObjectPresent(Response response) {
        var data = response.jsonPath().getMap("data");
        assertThat(data)
                .as("Ответ не пустой и содержит один объект")
                .isNotNull()
                .hasSize(1);
    }

    @Test
    @DisplayName("POST /goods/add -> 200, когда запрос валидный")
    void addProduct_returns200_onValidPayload() {
        String expectedName = randomName();
        double expectedPrice = randomPrice();
        Response response = addGood(new ProductRequest(expectedName, expectedPrice));

        assertStatusCode(response, 200, "Ожидаем статус 200");
        assertDataObjectPresent(response);

        int productId = response.jsonPath().getInt("data.id");
        deleteProduct(productId);
    }

    @Test
    @DisplayName("POST /goods/add -> 400, когда отсутствует имя")
    void addProduct_returns400_whenNameMissing() {
        Response response = addGood(Map.of("price", 9.99));

        assertStatusCode(response, 400, "Ожидаем ошибку из за отсутствия name");
    }

    @Test
    @DisplayName("POST /goods/add -> 400, когда имя неуникальное")
    void addProduct_returns400_whenNameNotUnique() {
        ProductData product = createProduct();
        Response response = addGood(new ProductRequest(product.name(), product.price()));

        assertStatusCode(response, 400,
                String.format("Ожидаем ошибку - продукт с именем %s уже существует", product.name()));
        deleteProduct(product.id());
    }

}
