package com.tealium.example.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Store {

    static final List<Product> sProducts;
    static final Random sRandom = new Random();

    static {
        sProducts = new ArrayList<>();

        sProducts.add(new Product("SKU-111", "Product 111", "Category 1", "Variant 1", "Brand 1", 10.00));
        sProducts.add(new Product("SKU-222", "Product 222", "Category 2", "Variant 2", "Brand 2", 20.00));
        sProducts.add(new Product("SKU-333", "Product 333", "Category 3", "Variant 3", "Brand 3", 30.00));
        sProducts.add(new Product("SKU-444", "Product 444", "Category 4", "Variant 4", "Brand 4", 40.00));
        sProducts.add(new Product("SKU-555", "Product 555", "Category 5", "Variant 5", "Brand 5", 50.00));
        sProducts.add(new Product("SKU-666", "Product 666", "Category 6", "Variant 6", "Brand 6", 60.00));
        sProducts.add(new Product("SKU-777", "Product 777", "Category 7", "Variant 7", "Brand 7", 70.00));
        sProducts.add(new Product("SKU-888", "Product 888", "Category 8", "Variant 8", "Brand 8", 80.00));
        sProducts.add(new Product("SKU-999", "Product 999", "Category 9", "Variant 9", "Brand 9", 90.00));
    }

    private Store() {
    }

    public static void addProduct(Product p) {
        sProducts.add(p);
    }

    public static int getProductCount() {
        return sProducts.size();
    }

    public static Product getProduct(int index) {
        if (index < 0 || index >= sProducts.size()) {
            return null; // out of bounds
        }
        return sProducts.get(index);
    }

    public static Product getProduct() {
        int index = sRandom.nextInt(getProductCount() - 1);
        return sProducts.get(index);
    }
}
