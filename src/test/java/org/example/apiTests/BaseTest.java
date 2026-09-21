package org.example.apiTests;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.example.config.ConfigReader;

public abstract class BaseTest {
    protected static final String BASE_URI = ConfigReader.getApiUrl();
    protected static final String USERNAME = ConfigReader.getAdminUsername();
    protected static final String PASSWORD = ConfigReader.getAdminPassword();
    protected static final RequestSpecification rs =
            new RequestSpecBuilder()
                    .setBaseUri(BASE_URI)
                    .setAuth(RestAssured.basic(USERNAME, PASSWORD))
                    .setContentType(ContentType.JSON)
                    .build();
}
