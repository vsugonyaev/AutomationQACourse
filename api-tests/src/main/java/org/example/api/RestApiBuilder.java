package org.example.api;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.example.common.config.LogMode;
import org.example.common.config.TestConfig;

/**
 * Собирает спецификацию запроса к тестовому API: базовый URL, таймауты,
 * Allure-фильтр, логирование и (опционально) авторизацию.
 */
public final class RestApiBuilder {

    private static final String CONNECTION_TIMEOUT = "http.connection.timeout";
    private static final String SOCKET_TIMEOUT = "http.socket.timeout";

    private final RequestSpecBuilder builder;

    private RestApiBuilder() {
        int timeoutMs = TestConfig.apiTimeoutMs();
        builder = new RequestSpecBuilder()
                .setBaseUri(TestConfig.apiUrl())
                .setContentType(ContentType.JSON)
                .setConfig(RestAssuredConfig.config().httpClient(HttpClientConfig.httpClientConfig()
                        .setParam(CONNECTION_TIMEOUT, timeoutMs)
                        .setParam(SOCKET_TIMEOUT, timeoutMs)))
                .addFilter(new AllureRestAssured());
        addLogging(TestConfig.logMode());
    }

    public static RestApiBuilder builder() {
        return new RestApiBuilder();
    }

    public RestApiBuilder withAdminAuth() {
        return withBasicAuth(TestConfig.adminUsername(), TestConfig.adminPassword());
    }

    public RestApiBuilder withBasicAuth(String username, String password) {
        builder.setAuth(RestAssured.preemptive().basic(username, password));
        return this;
    }

    public RequestSpecification build() {
        return builder.build();
    }

    private void addLogging(LogMode mode) {
        switch (mode) {
            case FULL -> builder.addFilter(new RequestLoggingFilter()).addFilter(new ResponseLoggingFilter());
            case BASIC -> builder.addFilter(new ErrorLoggingFilter());
            case NONE -> {
            }
        }
    }
}
