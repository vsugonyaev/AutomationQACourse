package org.example.apiTests;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.example.apiTests.SharedGoodsData.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(2)
@Tag("Theme4.2")
class GoodsDeleteTest extends BaseTest {

    @Step("Отправить DELETE /goods/{id}")
    private Response deleteGood(int id) {
        return given()
                .spec(rs)
                .log().all()
                .when()
                .delete("/goods/{id}", id)
                .then()
                .log().all()
                .extract()
                .response();
    }

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

    @Test
    @DisplayName("DELETE /goods/{id} -> 200, успешное удаление существуеющего продукта")
    void deleteProduct_returns200_whenExists() {
        ProductData product = createProduct();
        Response response = deleteGood(product.id());

        assertStatusCode(response, 200, "Ожидаем успешное удаление продукта");

        //Проверим что продукт действительно удален с помощью метода GoodGet
        Response getResponce = getGood(product.id());
        assertStatusCode(getResponce, 404, "Ожидается что продукт с таким id был удален, поэтому Get должен вернуть 404");
    }

    @Test
    @DisplayName("DELETE /goods/{id} -> 404 если продукта не существует")
    void deleteProduct_returns404_whenMissing() {
        ProductData product = createProduct();
        deleteProduct(product.id());
        Response response = deleteGood(product.id());
        assertStatusCode(response, 404, "Ожидаем 404, т.к. продукт был удален ранее");
    }
}
