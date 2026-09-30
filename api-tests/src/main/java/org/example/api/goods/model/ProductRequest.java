package org.example.api.goods.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/** Тело запросов POST /goods/add и PATCH /goods/{id}. Пустые поля в JSON не попадают. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductRequest(String name, Double price) {

    public static ProductRequest withoutName(double price) {
        return new ProductRequest(null, price);
    }
}
