package org.example.api.goods.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/** Товар в ответах GET /goods/{id}, PATCH /goods/{id} и GET /goods/list. */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ProductData(long id, String name, double price) {
}
