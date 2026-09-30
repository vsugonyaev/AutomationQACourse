package org.example.apiTests;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.example.apiTests.BaseTest.rs;


public final class SharedGoodsData {

    private SharedGoodsData() {
    }
    private static final Random random = new Random();

    public static String randomName() {

        return "Тестовый товар_" + random.nextInt();
    }
    public static double randomPrice() {

        return Math.round(random.nextDouble() * 10000) / 100.0;
    }

    record ProductRequest(String name, double price) {}
    public record ProductData(
            int id,
            String name,
            double price
    ) {}

    @Step("Отправить POST /goods/add с товаром {name}, цена {price}")
    private static Response postCreateProduct(String name, double price) {
        return given()
                .spec(rs)
                .body(new ProductRequest(name, price))
                .log().all()
                .when()
                .post("/goods/add")
                .then()
                .log().all()
                .extract()
                .response();
    }

    @Step("Проверить, что продукт успешно создан (статус 200)")
    private static void assertProductCreated(Response response) {
        assertThat(response.statusCode())
                .as("Не удалось создать тестовый продукт")
                .isEqualTo(200);
    }

    @Step("Создать тестовый продукт через API")
    public static ProductData createProduct() {
        String name = randomName();
        double price = randomPrice();

        Response response = postCreateProduct(name, price);
        assertProductCreated(response);

        int id = response.jsonPath().getInt("data.id");

        return new ProductData(id, name, price);
    }

    @Step("Отправить DELETE /goods/{id}")
    private static Response deleteGoodById(int id) {
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

    @Step("Проверить, что продукт с id {id} успешно удалён (статус 200)")
    private static void assertProductDeleted(Response response, int id) {
        assertThat(response.statusCode())
                .as("Не удалось удалить продукт с id %s", id)
                .isEqualTo(200);
    }

    @Step("Удалить продукт с id {id} через API")
    public static void deleteProduct(int id) {
        Response response = deleteGoodById(id);
        assertProductDeleted(response, id);
    }

    @Step("Отправить GET /goods/list")
    private static Response getGoodsList() {
        return given()
                .spec(rs)
                .log().all()
                .when()
                .get("/goods/list")
                .then()
                .log().all()
                .extract()
                .response();
    }

    @Step("Удалить все продукты через API")
    public static void deleteAllProducts() {
        Response response = getGoodsList();
        List<Integer> allId = response.jsonPath().getList("goods.id");
        for (Integer integer : allId) {
            deleteProduct(integer);
        }

    }

}
