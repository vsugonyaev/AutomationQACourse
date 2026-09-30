package org.example.api.tests;

import org.example.api.goods.GoodsApi;
import org.example.api.goods.GoodsSteps;
import org.example.api.goods.model.ProductData;
import org.example.api.goods.model.ProductRequest;
import org.example.common.data.TestData;
import org.junit.jupiter.api.AfterEach;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseApiTest {

    protected final GoodsApi goodsApi = GoodsApi.asAdmin();
    protected final GoodsApi anonymousGoodsApi = GoodsApi.anonymous();
    protected final GoodsSteps goodsSteps = new GoodsSteps(goodsApi);

    private final List<Long> createdIds = new ArrayList<>();

    protected ProductData givenProduct() {
        ProductData product = goodsSteps.createRandomProduct();
        trackForCleanup(product.id());
        return product;
    }

    protected ProductRequest randomProductRequest() {
        return new ProductRequest(TestData.productName(), TestData.price());
    }

    protected void trackForCleanup(long id) {
        createdIds.add(id);
    }

    @AfterEach
    void deleteCreatedProducts() {
        createdIds.forEach(goodsSteps::deleteQuietly);
    }
}
