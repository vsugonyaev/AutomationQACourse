package org.example.common.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Properties;

/**
 * Единая точка доступа к параметрам стенда и тестовым данным.
 * Порядок поиска значения: системное свойство -> переменная окружения -> config.properties.
 */
public final class TestConfig {

    private static final String CONFIG_FILE = "config.properties";
    private static final Properties PROPERTIES = load();

    static {
        printConfig();
    }

    private TestConfig() {
    }

    // ---------- Стенд ----------

    public static String standUrl() {
        return get("stand.url");
    }

    public static String storefrontPath() {
        return get("storefront.path");
    }

    public static String adminPath() {
        return get("admin.path");
    }

    public static String apiUrl() {
        return get("api.url");
    }

    // ---------- Учётные данные ----------

    public static String adminUsername() {
        return get("admin.username");
    }

    public static String adminPassword() {
        return get("admin.password");
    }

    // ---------- API ----------

    public static int apiTimeoutMs() {
        return Integer.parseInt(get("api.timeout.ms"));
    }

    public static int goodsListMaxSize() {
        return Integer.parseInt(get("api.goods.list.max.size"));
    }

    public static LogMode logMode() {
        return LogMode.valueOf(get("log.mode").toUpperCase(Locale.ROOT));
    }

    // ---------- UI ----------

    public static String browser() {
        return get("ui.browser");
    }

    public static String browserSize() {
        return get("ui.browser.size");
    }

    public static boolean headless() {
        return Boolean.parseBoolean(get("ui.headless"));
    }

    public static long elementTimeoutMs() {
        return Long.parseLong(get("ui.element.timeout.ms"));
    }

    // ---------- Бизнес-правила магазина ----------

    public static String shopName() {
        return get("shop.name");
    }

    public static int orderLimit() {
        return Integer.parseInt(get("shop.order.limit"));
    }

    // ---------- Тестовые данные ----------

    public static String productNamePrefix() {
        return get("test.product.name.prefix");
    }

    public static double productPriceMin() {
        return Double.parseDouble(get("test.product.price.min"));
    }

    public static double productPriceMax() {
        return Double.parseDouble(get("test.product.price.max"));
    }

    public static long missingProductId() {
        return Long.parseLong(get("test.missing.product.id"));
    }

    public static double cartFirstProductPrice() {
        return Double.parseDouble(get("test.cart.first.product.price"));
    }

    public static double cartSecondProductPrice() {
        return Double.parseDouble(get("test.cart.second.product.price"));
    }

    // ---------- Ожидаемые сообщения API ----------

    public static String apiAddSuccessMessage() {
        return get("api.message.add.success");
    }

    public static String apiNotFoundMessage(long id) {
        return String.format(get("api.message.not.found.template"), id);
    }

    public static String apiNegativePriceMessage() {
        return get("api.message.negative.price");
    }

    // ---------- Ожидаемые сообщения UI ----------

    public static String productAddedMessage() {
        return get("ui.message.product.added");
    }

    public static String productUpdatedMessage(long id) {
        return String.format(get("ui.message.product.updated.template"), id);
    }

    public static String addedToCartMessage(String productName, int quantity) {
        return String.format(get("ui.message.added.to.cart.template"), productName, quantity);
    }

    public static String orderAcceptedMessage() {
        return get("ui.message.order.accepted");
    }

    public static String orderPlacedTitle() {
        return get("ui.message.order.title");
    }

    public static String orderLimitExceededMessage(int orderTotal) {
        return String.format(get("ui.message.order.limit.exceeded.template"), shopName(), orderTotal, orderLimit());
    }

    public static String loginErrorMessage() {
        return get("ui.message.login.error");
    }

    // ---------- Внутреннее ----------

    private static String get(String key) {
        String value = System.getProperty(key);
        if (value == null) {
            value = System.getenv(key.replace('.', '_').toUpperCase(Locale.ROOT));
        }
        if (value == null) {
            value = PROPERTIES.getProperty(key);
        }
        if (value == null) {
            throw new IllegalStateException("В конфиге отсутствует параметр: " + key);
        }
        return value.trim();
    }

    private static Properties load() {
        Properties properties = new Properties();
        try (InputStream in = TestConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (in == null) {
                throw new IllegalStateException("Файл конфигурации не найден в classpath: " + CONFIG_FILE);
            }
            properties.load(new InputStreamReader(in, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new IllegalStateException("Не удалось прочитать файл конфигурации: " + CONFIG_FILE, e);
        }
        return properties;
    }

    private static void printConfig() {
        System.out.println("======================================");
        System.out.println("       ПАРАМЕТРЫ КОНФИГУРАЦИИ");
        System.out.println("======================================");
        System.out.println("URL стенда:                    " + standUrl());
        System.out.println("URL API:                       " + apiUrl());
        System.out.println("Таймаут поиска элементов, мс:  " + elementTimeoutMs());
        System.out.println("Таймаут API, мс:               " + apiTimeoutMs());
        System.out.println("Режим логирования:             " + logMode());
        System.out.println("Браузер:                       " + browser() + ", " + browserSize()
                + (headless() ? ", headless" : ""));
        System.out.println("Лимит заказа:                  " + orderLimit());
        System.out.println("======================================");
    }
}
