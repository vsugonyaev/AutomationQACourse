package org.example.api.goods.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

/** Ответ POST /goods/add: {"data": {"id": N}, "message": "..."}. */
@JsonIgnoreProperties(ignoreUnknown = true)
public record AddGoodResult(Map<String, Long> data, String message) {

    private static final String ID_KEY = "id";

    public Long createdId() {
        return data == null ? null : data.get(ID_KEY);
    }
}
