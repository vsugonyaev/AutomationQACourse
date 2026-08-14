import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class RESTAssuredSecondTaskTest {
    private static final String BASE_URI = "http://localhost:8080";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "secret123";
    private static final Random random = new Random();
    private final String name = "Тестовый товар_" + random.nextInt();
    private final Double price = Math.round(random.nextDouble() * 10000) / 100.0;
    private static final RequestSpecification rs =
            new RequestSpecBuilder()
                    .setBaseUri(BASE_URI)
                    .build();

    private final List<Long> createdIds = new ArrayList<>();
    private Long productId;
    private String productName;
    @Test
    @DisplayName("POST /goods/add -> 200 если запрос валидный")
    void addProduct_returns200_onValidPayload() {

        Response response = given()
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
                .post("/goods/add");

        assertThat(response.statusCode())
                .as("Статус должен быть 200")
                .isEqualTo(200);
        assertThat(response.contentType())
                .as("Ответ должен быть JSON")
                .contains(ContentType.JSON.toString());

        var data = response.jsonPath().getMap("data");
        assertThat(data)
                .as("Массив в ответе должен быть не пустым")
                .isNotNull()
                .
    }

    @Test
    @DisplayName("POST /goods/add -> 400 когда отсутсвует обязательное name")
    void addProduct_returns400_whenNameMissing() {
        Map<String, Object> payload = Map.of(
                "price", 9.99
        );

        Response response = given()
                .spec(spec)
                .body(payload)
                .when()
                .post("/goods/add");

        assertThat(response.statusCode()).isEqualTo(400);

        String message = response.jsonPath().getString("message");
        assertThat(message).isNotBlank();
    }
}
