package org.example.apiTests;

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
    /*@BeforeAll
    static void clearAllGoods() {
        deleteAllProducts();
    }*/

    @Test
    @DisplayName("POST /goods/add -> 200, когда запрос валидный")
    void addProduct_returns200_onValidPayload() {
        String expectedName = randomName();
        double expectedPrice = randomPrice();
        Response response = given()
                .spec(rs)
                .body(new ProductRequest(expectedName, expectedPrice))
                .log().all()
                .when()
                .post("/goods/add")
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


        int productId = response.jsonPath().getInt("data.id");
        deleteProduct(productId);

    }

    @Test
    @DisplayName("POST /goods/add -> 400, когда отсутствует имя")
    void addProduct_returns400_whenNameMissing() {
        Response response = given()
                .spec(rs)
                .body(Map.of("price", 9.99))
                .log().all()
                .when()
                .post("/goods/add")
                .then()
                .log().all()
                .extract()
                .response();

        assertThat(response.statusCode())
                .as("Ожидаем ошибку из за отсутствия name")
                .isEqualTo(400);
    }

    @Test
    @DisplayName("POST /goods/add -> 400, когда имя неуникальное")
    void addProduct_returns400_whenNameNotUnique() {
        ProductData product = createProduct();
        Response response = given()
                .spec(rs)
                .body(new ProductRequest(product.name(), product.price()))
                .log().all()
                .when()
                .post("/goods/add")
                .then()
                .log().all()
                .extract()
                .response();

        assertThat(response.statusCode())
                .as("Ожидаем ошибку - продукт с именем %s уже существует", product.name())
                .isEqualTo(400);
        deleteProduct(product.id());
    }
}
