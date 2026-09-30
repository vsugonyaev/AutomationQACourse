package org.example.api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.example.api.goods.model.AddGoodResult;
import org.example.api.goods.model.GoodsList;
import org.example.api.goods.model.ProductData;

import java.util.List;

/** Проверки ответов тестового API. Каждая проверка — отдельный шаг в Allure-отчёте. */
public final class ApiAssert {

    private final Response response;

    private ApiAssert(Response response) {
        this.response = response;
    }

    public static ApiAssert assertThat(Response response) {
        return new ApiAssert(response);
    }

    @Step("Проверить, что код ответа = {expectedStatus}")
    public ApiAssert statusCode(int expectedStatus) {
        Assertions.assertThat(response.statusCode())
                .as("Код ответа. Тело ответа: %s", response.asString())
                .isEqualTo(expectedStatus);
        return this;
    }

    @Step("Проверить, что вернулся id созданного товара и сообщение '{expectedMessage}'")
    public ApiAssert createdWithMessage(String expectedMessage) {
        AddGoodResult result = response.as(AddGoodResult.class);
        Assertions.assertThat(result.createdId())
                .as("id созданного товара")
                .isNotNull()
                .isPositive();
        Assertions.assertThat(result.message())
                .as("Сообщение в ответе")
                .isEqualTo(expectedMessage);
        return this;
    }

    @Step("Проверить, что в ответе товар {expected}")
    public ApiAssert productEquals(ProductData expected) {
        Assertions.assertThat(response.as(ProductData.class))
                .as("Товар в ответе")
                .isEqualTo(expected);
        return this;
    }

    @Step("Проверить, что в ответе товар с названием '{expectedName}' и ценой {expectedPrice}")
    public ApiAssert productHas(String expectedName, double expectedPrice) {
        ProductData actual = response.as(ProductData.class);
        Assertions.assertThat(actual.name()).as("Название товара").isEqualTo(expectedName);
        Assertions.assertThat(actual.price()).as("Цена товара").isEqualTo(expectedPrice);
        return this;
    }

    @Step("Проверить, что ответ содержит список товаров")
    public ApiAssert hasGoodsList() {
        Assertions.assertThat(goods()).as("Список товаров в ответе").isNotNull();
        return this;
    }

    @Step("Проверить, что список содержит товар {expected}")
    public ApiAssert listContains(ProductData expected) {
        Assertions.assertThat(goods()).as("Список товаров").contains(expected);
        return this;
    }

    @Step("Проверить, что в списке нет товара с id {id}")
    public ApiAssert listDoesNotContain(long id) {
        Assertions.assertThat(goods())
                .as("Товар с id %s не должен быть в списке", id)
                .extracting(ProductData::id)
                .doesNotContain(id);
        return this;
    }

    @Step("Проверить, что тело ответа содержит '{expectedText}'")
    public ApiAssert bodyContains(String expectedText) {
        Assertions.assertThat(response.asString()).as("Тело ответа").contains(expectedText);
        return this;
    }

    @Step("Проверить ошибку валидации поля '{field}': '{expectedMessage}'")
    public ApiAssert validationError(String field, String expectedMessage) {
        Assertions.assertThat(response.jsonPath().getString(field))
                .as("Сообщение валидации поля %s", field)
                .isEqualTo(expectedMessage);
        return this;
    }

    private List<ProductData> goods() {
        return response.as(GoodsList.class).goods();
    }
}
