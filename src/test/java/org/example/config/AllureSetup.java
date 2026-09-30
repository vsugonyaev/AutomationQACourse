package org.example.config;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.restassured.AllureRestAssured;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;

public final class AllureSetup {

    private static volatile boolean initialized = false;

    private AllureSetup() {
    }

    public static synchronized void init() {
        if (initialized) {
            return;
        }

        RestAssured.filters(new AllureRestAssured());

        SelenideLogger.addListener(
                "AllureSelenide",
                new AllureSelenide().screenshots(true).savePageSource(true)
        );

        initialized = true;
    }
}
