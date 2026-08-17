import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import io.restassured.response.Response;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.*;

@Tag("Theme4.1")
public class RESTAssuredFirstTaskTest {
    private static final RequestSpecification rs =
            new RequestSpecBuilder()
                    .setBaseUri("http://localhost:8080/")
                    .build();
    private static final Random random = new Random();

    @Test
    @DisplayName("Проверка Get goods/list через given,when,then")
    void testGetGoods(){
        given()
                .baseUri("http://localhost:8080/")
                .queryParam("page", 0)
                .queryParam("size", 1000)
                .log().all()
                .when()
                .get("/goods/list")
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                //.body(equalTo("[]"))
                .body("goods", empty());
    }
    @Test
    @DisplayName("Проверка Get goods/list через Specification")
    void anotherTestGetGoods(){
        given()
                .spec(rs)
                .queryParam("page", 0)
                .queryParam("size", 1000)
                .log().all()
                .when()
                .get("/goods/list")
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                //.body(equalTo("[]"))
                .body("goods", empty());
    }
    @Test
    @DisplayName("Проверка создания и отображения товара")
    void testAddNewGood() {
        String name = "Туфля_" + random.nextInt();
        Double price = Math.round(random.nextDouble() * 10000) / 100.0;
        int id = given()
                    .spec(rs)
                    .auth()
                    .basic("admin", "secret123")
                    .contentType(ContentType.JSON)
                    .body("""
                        {
                          "name": "%s",
                          "price": %s
                        }
                        """.formatted(name, price))
                    .when()
                    .post("/goods/add")
                    .then()
                    .log().ifValidationFails()
                    .statusCode(200)
                    .extract()
                    .path("data.id");

        given()
                .spec(rs)
                .queryParam("page", 0)
                .queryParam("size", 1000)
                .log().all()
                .when()
                .get("/goods/list")
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("goods.find { it.id == " + id + " }.name",
                        equalTo(name))
                .body("goods.find { it.id == " + id + " }.price",
                        equalTo(price));
    }

    @Test
    @DisplayName("AssertJ проверка создания и отображения товара")
    void anotherTestNewGood() {
        String name = "Дамский угодник_" + random.nextInt();
        Double price = Math.round(random.nextDouble() * 10000) / 100.0;
        int id = given()
                .spec(rs)
                .auth()
                .basic("admin", "secret123")
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "name": "%s",
                          "price": %s
                        }
                        """.formatted(name, price))
                .when()
                .post("/goods/add")
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .extract()
                .path("data.id");

        var responce = given()
                .spec(rs)
                .queryParam("page", 0)
                .queryParam("size", 1000)
                .log().all()
                .when()
                .get("/goods/list")
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .extract();
        String actualName = responce
                .jsonPath()
                .getString("goods.find { it.id == " + id + " }.name");

        Float actualPrice = responce
                .jsonPath()
                .getFloat("goods.find { it.id == " + id + " }.price");

        assertThat(actualName)
                .as("Название должно быть: %s, но пришло: %d", name, actualName)
                .isEqualTo(name);
        assertThat(actualPrice)
                .as("Цена должна быть: %s, но пришла: %d", price, actualPrice)
                .isEqualTo(price.floatValue());
    }
}
