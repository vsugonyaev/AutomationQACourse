package org.example.apiTests;

import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.example.apiTests.SharedGoodsData.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(2)
@Tag("Theme4.2")
class GoodsDeleteTest extends BaseTest {

    @Test
    @DisplayName("DELETE /goods/{id} -> 200, успешное удаление существуеющего продукта")
    void deleteProduct_returns200_whenExists() {
        ProductData product = createProduct();
        Response response = given()
                .spec(rs)
                .log().all()
                .when()
                .delete("/goods/{id}", product.id())
                .then()
                .log().all()
                .extract()
                .response();

        assertThat(response.statusCode())
                .as("Ожидаем успешное удаление продукта")
                .isEqualTo(200);

        //Проверим что продукт действительно удален с помощью метода GoodGet
        Response getResponce = given()
                .spec(rs)
                .log().all()
                .when()
                .get("/goods/{id}", product.id())
                .then()
                .log().all()
                .extract()
                .response();
        assertThat(getResponce.statusCode())
                .as("Ожидается что продукт с таким id был удален, поэтому Get должен вернуть 404")
                .isEqualTo(404);
    }

    @Test
    @DisplayName("DELETE /goods/{id} -> 404 если продукта не существует")
    void deleteProduct_returns404_whenMissing() {
        ProductData product = createProduct();
        deleteProduct(product.id());
        Response response = given()
                .spec(rs)
                .log().all()
                .when().delete("/goods/{id}", product.id())
                .then()
                .log().all()
                .extract().
                response();
        assertThat(response.statusCode())
                .as("Ожидаем 404, т.к. продукт был удален ранее")
                .isEqualTo(404);
    }
}