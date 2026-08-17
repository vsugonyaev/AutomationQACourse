package org.example.apiTests;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public abstract class BaseTest {
    protected static final String BASE_URI = "http://localhost:8080";
    protected static final String USERNAME = "admin";
    protected static final String PASSWORD = "secret123";
    protected static final RequestSpecification rs =
            new RequestSpecBuilder()
                    .setBaseUri(BASE_URI)
                    .setAuth(RestAssured.basic(USERNAME, PASSWORD))
                    .setContentType(ContentType.JSON)
                    .build();
}
