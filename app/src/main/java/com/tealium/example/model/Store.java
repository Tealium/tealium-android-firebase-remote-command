package com.tealium.example.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Store {

    static final List<Product> mProducts;
    static final Random mRandom = new Random();

    static {
        mProducts = new ArrayList<>();

        mProducts.add(new Product("SKU-111", "Product 111", "Category 1", 1, 10L));
        mProducts.add(new Product("SKU-222", "Product 222", "Category 2", 2, 20L));
        mProducts.add(new Product("SKU-333", "Product 333", "Category 3", 3, 30L));
        mProducts.add(new Product("SKU-444", "Product 444", "Category 4", 4, 40L));
        mProducts.add(new Product("SKU-555", "Product 555", "Category 5", 5, 50L));
        mProducts.add(new Product("SKU-666", "Product 666", "Category 6", 6, 60L));
        mProducts.add(new Product("SKU-777", "Product 777", "Category 7", 7, 70L));
        mProducts.add(new Product("SKU-888", "Product 888", "Category 8", 8, 80L));
        mProducts.add(new Product("SKU-999", "Product 999", "Category 9", 9, 90L));
    }

    private Store() {
    }

    public static void addProduct(Product p) {
        mProducts.add(p);
    }

    public static int getProductCount() {
        return mProducts.size();
    }

    public static Product getProduct(int index) {
        if (index < 0 || index >= mProducts.size()) {
            return null; // out of bounds
        }
        return mProducts.get(index);
    }

    public static Product getProduct() {
        int index = mRandom.nextInt(getProductCount() - 1);
        return mProducts.get(index);
    }

}
