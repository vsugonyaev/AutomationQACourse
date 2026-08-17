package org.example.apiTests;

import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.example.apiTests.SharedGoodsData.*;

@Tag("Theme4.2")
class GoodsPatchTest extends BaseTest {

    @Test
    @DisplayName("PATCH /goods/{id} -> 200, удачное изменение имени продукта")
    void patchProduct_returns200_onValidNameUpdate() {
        ProductData product = createProduct();
        String expectedName = randomName();
        Response response = given()
                .spec(rs)
                .body(new ProductRequest(expectedName, product.price()))
                .log().all()
                .when()
                .patch("/goods/{id}", product.id())
                .then()
                .log().all()
                .extract().
                response();

        assertThat(response.statusCode())
                .as("Ожидаем успех и статус 200")
                .isEqualTo(200);

        String actualName = response.jsonPath().getString("name");
        double actualPrice = response.jsonPath().getDouble("price");

        assertThat(actualName)
                .as("Ожидаем название продукта: %a, но получили %b", expectedName, actualName)
                .isEqualTo(product.name());
        assertThat(actualPrice)
                .as("Ожидаем цену продукта %s, а получили %d", product.price(), actualPrice)
                .isEqualTo(product.price());
        deleteProduct(product.id());
    }

    @Test
    @DisplayName("PATCH /goods/{id} -> 200, удачное изменение цены продукта")
    void patchProduct_returns200_onValidPriceUpdate() {
        ProductData product = createProduct();
        double anotherPrice = randomPrice();
        Response response = given()
                .spec(rs)
                .body(new ProductRequest(product.name(), anotherPrice))
                .log().all()
                .when()
                .patch("/goods/{id}", product.id())
                .then()
                .log().all()
                .extract()
                .response();

        assertThat(response.statusCode())
                .as("Ожидаем успех и статус 200")
                .isEqualTo(200);

        String actualName = response.jsonPath().getString("name");
        double actualPrice = response.jsonPath().getDouble("price");

        assertThat(actualName)
                .as("Ожидаем название продукта: %a, но получили %b", product.name(), actualName)
                .isEqualTo(product.name());
        assertThat(actualPrice)
                .as("Ожидаем цену продукта %s, а получили %d", anotherPrice, actualPrice)
                .isEqualTo(anotherPrice);
        deleteProduct(product.id());
    }

    @Test
    @DisplayName("PATCH /goods/{id} -> 400, когда пытаемся установить уже существуещее название продукта")
    void patchProduct_returns400_onDuplicateName() {
        ProductData product = createProduct();

        Response patchResponse = given()
                .spec(rs)
                .body(new ProductRequest(product.name(), randomPrice()))
                .when()
                .patch("/goods/{id}", product.id())
                .then()
                .log().all()
                .extract()
                .response();

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
                .patch("/goods/{id}", 9999)
                .then()
                .log().all()
                .extract()
                .response();;

        assertThat(response.statusCode())
                .as("Ожидаем что такого продукта не существует, статус 404")
                .isEqualTo(404);
    }
}
