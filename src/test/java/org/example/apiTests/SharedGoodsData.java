package org.example.apiTests;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.example.apiTests.BaseTest.*;


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

    public static ProductData createProduct() {
        String name = randomName();
        double price = randomPrice();

        Response response = given()
                .spec(rs)
                .body(new ProductRequest(name, price))
                .when()
                .post("/goods/add")
                .then()
                .log().all()
                .extract()
                .response();

        assertThat(response.statusCode())
                .as("Не удалось создать тестовый продукт")
                .isEqualTo(200);

        int id = response.jsonPath().getInt("data.id");

        return new ProductData(id, name, price);
    }

    public static void deleteProduct(int id) {
        Response response = given()
                .spec(rs)
                .when()
                .delete("/goods/{id}", id)
                .then()
                .log().all()
                .extract()
                .response();

        assertThat(response.statusCode())
                .as("Не удалось удалить продукт с id %s", id)
                .isEqualTo(200);
    }

}