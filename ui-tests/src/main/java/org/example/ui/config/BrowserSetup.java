package org.example.ui.config;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.example.common.config.TestConfig;

/** Настройка Selenide из конфига и подключение Allure Selenide Listener. */
public final class BrowserSetup {

    private static final String ALLURE_LISTENER = "AllureSelenide";

    private static boolean configured;

    private BrowserSetup() {
    }

    public static synchronized void configure() {
        if (configured) {
            return;
        }
        Configuration.baseUrl = TestConfig.standUrl();
        Configuration.browser = TestConfig.browser();
        Configuration.browserSize = TestConfig.browserSize();
        Configuration.headless = TestConfig.headless();
        Configuration.timeout = TestConfig.elementTimeoutMs();

        SelenideLogger.addListener(ALLURE_LISTENER, new AllureSelenide().screenshots(true).savePageSource(true));
        configured = true;
    }
}
