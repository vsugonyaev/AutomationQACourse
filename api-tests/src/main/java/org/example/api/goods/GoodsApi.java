package org.example.api.goods;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.example.api.BasicApi;
import org.example.api.RestApiBuilder;

/** basicApi-класс для эндпоинтов /goods: только вызовы, без проверок. */
public class GoodsApi extends BasicApi {

    private static final String ADD = "/goods/add";
    private static final String BY_ID = "/goods/{id}";
    private static final String LIST = "/goods/list";

    private static final String ID_PARAM = "id";
    private static final String PAGE_PARAM = "page";
    private static final String SIZE_PARAM = "size";

    public GoodsApi(RequestSpecification specification) {
        super(specification);
    }

    public static GoodsApi asAdmin() {
        return new GoodsApi(RestApiBuilder.builder().withAdminAuth().build());
    }

    public static GoodsApi anonymous() {
        return new GoodsApi(RestApiBuilder.builder().build());
    }

    @Step("POST /goods/add, тело: {body}")
    public Response addGood(Object body) {
        return request().body(body).post(ADD);
    }

    @Step("GET /goods/{id}")
    public Response getGood(long id) {
        return request().pathParam(ID_PARAM, id).get(BY_ID);
    }

    @Step("GET /goods/list?page={page}&size={size}")
    public Response listGoods(int page, int size) {
        return request().queryParam(PAGE_PARAM, page).queryParam(SIZE_PARAM, size).get(LIST);
    }

    @Step("GET /goods/list без параметров пагинации")
    public Response listGoods() {
        return request().get(LIST);
    }

    @Step("PATCH /goods/{id}, тело: {body}")
    public Response patchGood(long id, Object body) {
        return request().pathParam(ID_PARAM, id).body(body).patch(BY_ID);
    }

    @Step("DELETE /goods/{id}")
    public Response deleteGood(long id) {
        return request().pathParam(ID_PARAM, id).delete(BY_ID);
    }
}
