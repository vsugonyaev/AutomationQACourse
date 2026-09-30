package org.example.apiTests;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.example.apiTests.SharedGoodsData.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(4)
@Tag("Theme4.2")
class GoodsPatchTest extends BaseTest {

    @Step("Отправить PATCH /goods/{id} с телом {body}")
    private Response patchGood(int id, Object body) {
        return given()
                .spec(rs)
                .body(body)
                .log().all()
                .when()
                .patch("/goods/{id}", id)
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

    @Step("Проверить название и цену товара в ответе")
    private void assertProductData(Response response, String expectedName, double expectedPrice) {
        String actualName = response.jsonPath().getString("name");
        double actualPrice = response.jsonPath().getDouble("price");

        assertThat(actualName)
                .as("Ожидаем название продукта: %s, но получили %s", expectedName, actualName)
                .isEqualTo(expectedName);
        assertThat(actualPrice)
                .as("Ожидаем цену продукта %s, а получили %s", expectedPrice, actualPrice)
                .isEqualTo(expectedPrice);
    }

    @Test
    @DisplayName("PATCH /goods/{id} -> 200, удачное изменение имени продукта")
    void patchProduct_returns200_onValidNameUpdate() {
        ProductData product = createProduct();
        String expectedName = randomName();
        Response response = patchGood(product.id(), new ProductRequest(expectedName, product.price()));

        assertStatusCode(response, 200, "Ожидаем успех и статус 200");
        assertProductData(response, expectedName, product.price());
        deleteProduct(product.id());
    }

    @Test
    @DisplayName("PATCH /goods/{id} -> 200, удачное изменение цены продукта")
    void patchProduct_returns200_onValidPriceUpdate() {
        ProductData product = createProduct();
        double anotherPrice = randomPrice();
        Response response = patchGood(product.id(), new ProductRequest(product.name(), anotherPrice));

        assertStatusCode(response, 200, "Ожидаем успех и статус 200");
        assertProductData(response, product.name(), anotherPrice);
        deleteProduct(product.id());
    }

    @Test
    @DisplayName("PATCH /goods/{id} -> 400, когда пытаемся установить уже существуещее название продукта")
    void patchProduct_returns400_onDuplicateName() {
        ProductData firstProduct = createProduct();
        ProductData secondProduct = createProduct();
        Response patchResponse = patchGood(secondProduct.id(), new ProductRequest(firstProduct.name(), randomPrice()));

        assertStatusCode(patchResponse, 400, "Ожидаем ошибку, статус 400");
        deleteProduct(firstProduct.id());
        deleteProduct(secondProduct.id());
    }

    @Test
    @DisplayName("PATCH /goods/{id} -> 404, если продукта с таким id не существует")
    void patchProduct_returns404_whenMissing() {
        Response response = patchGood(9999, new ProductRequest(randomName(), randomPrice()));

        assertStatusCode(response, 404, "Ожидаем что такого продукта не существует, статус 404");
    }
}
