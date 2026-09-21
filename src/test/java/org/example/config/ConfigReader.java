package org.example.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public final class ConfigReader {

    private static final String CONFIG_FILE = "config.properties";
    private static final Properties properties = new Properties();

    static {
        try (InputStream in = ConfigReader.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (in == null) {
                throw new IllegalStateException("Файл конфигурации не найден в classpath: " + CONFIG_FILE);
            }
            properties.load(new InputStreamReader(in, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new IllegalStateException("Не удалось прочитать файл конфигурации: " + CONFIG_FILE, e);
        }
        printConfig();
    }

    private ConfigReader() {
    }

    private static String get(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new IllegalStateException("В конфиге отсутствует параметр: " + key);
        }
        return value.trim();
    }

    public static String getStandUrl() {
        return get("stand.url");
    }

    public static String getApiUrl() {
        return get("api.url");
    }

    public static long getElementTimeoutMs() {
        return Long.parseLong(get("element.search.timeout.ms"));
    }

    public static String getLogMode() {
        return get("log.mode");
    }

    public static String getAdminUsername() {
        return get("admin.username");
    }

    public static String getAdminPassword() {
        return get("admin.password");
    }

    public static String getStarterProductName() {
        return get("product.name");
    }

    public static double getStarterProductPrice() {
        return Double.parseDouble(get("product.price"));
    }

    private static void printConfig() {
        System.out.println("======================================");
        System.out.println("       ПАРАМЕТРЫ КОНФИГУРАЦИИ");
        System.out.println("======================================");
        System.out.println("URL стенда:                   " + getStandUrl());
        System.out.println("URL API:                      " + getApiUrl());
        System.out.println("Таймаут поиска элементов, мс:  " + getElementTimeoutMs());
        System.out.println("Режим логирования:             " + getLogMode());
        System.out.println("Стартовый товар:               " + getStarterProductName()
                + ", цена: " + getStarterProductPrice());
        System.out.println("======================================");
    }
}
