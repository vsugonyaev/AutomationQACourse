package org.example.ui.util;

import java.math.BigDecimal;

public final class PriceFormat {

    private PriceFormat() {
    }

    /** Цена так, как её печатает JavaScript на витрине и в админке: 15.0 -> "15", 20.01 -> "20.01". */
    public static String asShown(double price) {
        return BigDecimal.valueOf(price).stripTrailingZeros().toPlainString();
    }
}
