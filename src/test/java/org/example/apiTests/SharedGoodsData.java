package org.example.apiTests;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public final class SharedGoodsData {

    private SharedGoodsData() {
    }
    private static final Random random = new Random();
    public static String randomName() {
        return "Тестовый товар_" + random.nextInt();
    }

    public static double randomPrice() {
        return Math.round(random.nextDouble() * 10000) / 100.0;
    }
    record ProductRequest(String name, double price) {}
    public static Long productId;
    public static String productName;

    public static final List<Long> createdIds = Collections.synchronizedList(new ArrayList<>());

    public static void registerCreatedId(long id) {
        createdIds.add(id);
    }
}