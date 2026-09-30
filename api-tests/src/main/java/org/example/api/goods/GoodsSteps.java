package org.example.api.goods;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.example.api.goods.model.AddGoodResult;
import org.example.api.goods.model.GoodsList;
import org.example.api.goods.model.ProductData;
import org.example.api.goods.model.ProductRequest;
import org.example.common.config.TestConfig;
import org.example.common.data.TestData;

import java.util.List;

import static org.apache.http.HttpStatus.SC_OK;
import static org.example.api.ApiAssert.assertThat;

/** Бизнес-шаги поверх GoodsApi: подготовка и удаление тестовых данных. */
public class GoodsSteps {

    private static final int FIRST_PAGE = 0;

    private final GoodsApi goodsApi;

    public GoodsSteps(GoodsApi goodsApi) {
        this.goodsApi = goodsApi;
    }

    public static GoodsSteps asAdmin() {
        return new GoodsSteps(GoodsApi.asAdmin());
    }

    @Step("Подготовить товар через API: '{name}', цена {price}")
    public ProductData createProduct(String name, double price) {
        Response response = goodsApi.addGood(new ProductRequest(name, price));
        assertThat(response).statusCode(SC_OK);
        return new ProductData(response.as(AddGoodResult.class).createdId(), name, price);
    }

    @Step("Подготовить товар со случайными названием и ценой через API")
    public ProductData createRandomProduct() {
        return createProduct(TestData.productName(), TestData.price());
    }

    @Step("Подготовить id удалённого товара (товара с таким id больше нет)")
    public long deletedProductId() {
        ProductData product = createRandomProduct();
        assertThat(goodsApi.deleteGood(product.id())).statusCode(SC_OK);
        return product.id();
    }

    @Step("Найти id товаров с названием '{name}'")
    public List<Long> findIdsByName(String name) {
        Response response = goodsApi.listGoods(FIRST_PAGE, TestConfig.goodsListMaxSize());
        return response.as(GoodsList.class).goods().stream()
                .filter(product -> product.name().equals(name))
                .map(ProductData::id)
                .toList();
    }

    @Step("Удалить все товары на стенде")
    public void deleteAllProducts() {
        Response response = goodsApi.listGoods(FIRST_PAGE, TestConfig.goodsListMaxSize());
        assertThat(response).statusCode(SC_OK);
        response.as(GoodsList.class).goods()
                .forEach(product -> assertThat(goodsApi.deleteGood(product.id())).statusCode(SC_OK));
    }

    // Cleanup must not fail the test: the product may already be deleted by the test itself.
    @Step("Удалить тестовый товар id={id} (очистка)")
    public void deleteQuietly(long id) {
        goodsApi.deleteGood(id);
    }
}
