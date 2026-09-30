package org.example.api.goods.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/** Ответ GET /goods/list. */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GoodsList(List<ProductData> goods) {
}
