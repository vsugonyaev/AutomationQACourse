package org.example.api;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

/** Базовый класс для API-клиентов: хранит спецификацию, собранную RestApiBuilder. */
public abstract class BasicApi {

    private final RequestSpecification specification;

    protected BasicApi(RequestSpecification specification) {
        this.specification = specification;
    }

    protected RequestSpecification request() {
        return RestAssured.given().spec(specification);
    }
}
