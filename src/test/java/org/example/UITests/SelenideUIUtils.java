package org.example.UITests;

import com.codeborne.selenide.Configuration;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class SelenideUIUtils {

    public static final String BASE_URL = "http://localhost:8080/";
    public static final String ADMIN_URL = "http://localhost:8080/admin";
    private static final List<String> createdProducts = new ArrayList<>();

    public static void init() {
        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(org.openqa.selenium.PageLoadStrategy.NORMAL);
        Configuration.browserSize = "1920x1080";
        Configuration.browserCapabilities = options;
        Configuration.browser = "chrome";
        Configuration.timeout = 5000;
    }

    public static void auth() {
        open(ADMIN_URL);
        $("#username").shouldBe(visible).sendKeys("admin");
        $("#password").sendKeys("secret123");
        $("button.primary").click();
    }

    public static void createNewProduct(String name, double price) {
        System.out.println("Поступил запрос на создание товара: " + name);
        auth();
        $("#n-name").shouldBe(visible).sendKeys(name);
        $("#n-price").sendKeys(String.valueOf(price));
        $("#add-btn").click();
        System.out.println("Товар успешно создан!");
        hardRefresh(ADMIN_URL);
        createdProducts.add(name);
    }

    public static void deleteCreatedProduct(String name) {
        System.out.println("Поступил запрос на удаление товара: " + name);
        String xpathExpression = String.format(
                "//tr[td/input[@value='%s']]//button[contains(@class, 'btn-del')]",
                name
        );
        $x(xpathExpression).shouldBe(clickable).click();
        confirm(); // подтверждаем alert
        $x("//*[text()='Товар удален']").shouldBe(visible);
        $x(String.format("//tr[td/input[@value='%s']]", name)).shouldBe(hidden);
        System.out.println("Товар успешно удален!");
    }
    public static void deleteAllCreatedProducts() {
        auth();
        System.out.println("Удаляем все созданные товары: " + createdProducts);
        // Проходим по списку в обратном порядке, чтобы избежать проблем при удалении
        for (int i = createdProducts.size() - 1; i >= 0; i--) {
            String name = createdProducts.get(i);
            deleteCreatedProduct(name);
            createdProducts.remove(i); // удаляем из списка после попытки
        }
        hardRefresh(ADMIN_URL);
    }
    public static void hardRefresh(String url) {
        clearBrowserCookies();
        localStorage().clear();
        sessionStorage().clear();
        open(url);
    }
    public static void waitForToastToDisappear() {
        $("#toast-container").shouldBe(hidden);
    }
}