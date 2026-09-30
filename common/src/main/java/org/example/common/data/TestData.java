package org.example.common.data;

import org.example.common.config.TestConfig;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public final class TestData {

    private static final AtomicInteger COUNTER = new AtomicInteger();

    private TestData() {
    }

    /** Уникальное имя товара: префикс из конфига + метка запуска + порядковый номер. */
    public static String productName() {
        return TestConfig.productNamePrefix() + "_" + uniqueSuffix();
    }

    /** Случайная цена из диапазона, заданного в конфиге. */
    public static double price() {
        return priceBetween(TestConfig.productPriceMin(), TestConfig.productPriceMax());
    }

    /** Случайная цена с точностью до копеек в границах [min, max]. */
    public static double priceBetween(double min, double max) {
        double raw = ThreadLocalRandom.current().nextDouble(min, max);
        return BigDecimal.valueOf(raw).setScale(2, RoundingMode.DOWN).doubleValue();
    }

    /** Значение, гарантированно отличающееся от исходного (например, неверный логин). */
    public static String differentFrom(String value) {
        return value + "_" + uniqueSuffix();
    }

    // Fixed-width counter so no generated name is a prefix of another one.
    private static String uniqueSuffix() {
        return Long.toString(System.currentTimeMillis(), 36) + String.format("%04d", COUNTER.incrementAndGet());
    }
}
