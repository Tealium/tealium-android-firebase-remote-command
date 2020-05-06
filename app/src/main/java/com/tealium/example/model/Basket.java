package com.tealium.example.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Basket {

    private static Basket INSTANCE;
    private Map<Product, BasketProduct> products = new HashMap<>();

    private Basket() {
    }

    public static Basket getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Basket();
        }
        return INSTANCE;
    }

    public void addProduct(Product product) {
        addProduct(product, 1);
    }

    public void addProduct(Product product, final int quantity) {
        if (!products.containsKey(product)) {
            products.put(product, new BasketProduct(product, quantity));
        } else {
            Objects.requireNonNull(products.get(product)).quantity++;
        }
    }

    public void removeProduct(Product product) {
        removeProduct(product, 1);
    }

    public void removeProduct(final Product product, int quantity) {
        if (products.containsKey(product)) {
            BasketProduct p = products.get(product);
            if (p != null && p.quantity > quantity) {
                p.quantity -= quantity;
            } else {
                products.remove(product);
            }
        }
    }

    public List<String> getProductIds() {
        List<String> productIds = new ArrayList<>(products.size());
        for (BasketProduct p : products.values()) {
            productIds.add(p.product.getProductId());
        }
        return productIds;
    }

    public List<String> getProductNames() {
        List<String> productNames = new ArrayList<>(products.size());
        for (BasketProduct p : products.values()) {
            productNames.add(p.product.getProductName());
        }
        return productNames;
    }

    public List<String> getProductCategories() {
        List<String> productCategories = new ArrayList<>(products.size());
        for (BasketProduct p : products.values()) {
            productCategories.add(p.product.getProductCategory());
        }
        return productCategories;
    }

    public List<String> getProductVariants() {
        List<String> productVariants = new ArrayList<>(products.size());
        for (BasketProduct p : products.values()) {
            productVariants.add(p.product.getProductVariant());
        }
        return productVariants;
    }

    public List<String> getProductBrands() {
        List<String> productBrands = new ArrayList<>(products.size());
        for (BasketProduct p : products.values()) {
            productBrands.add(p.product.getProductBrand());
        }
        return productBrands;
    }

    public List<Integer> getProductQuantities() {
        List<Integer> productQuantities = new ArrayList<>(products.size());
        for (BasketProduct p : products.values()) {
            productQuantities.add(p.quantity);
        }
        return productQuantities;
    }

    public List<Double> getProductUnitPrices() {
        List<Double> productUnitPrice = new ArrayList<>(products.size());
        for (BasketProduct p : products.values()) {
            productUnitPrice.add(p.product.getProductUnitPrice());
        }
        return productUnitPrice;
    }

    public double getBasketTotalPrice() {
        double total = 0.0d;
        for (BasketProduct p : products.values()) {
            total += p.quantity * p.product.getProductUnitPrice();
        }
        return total;
    }

    public int getItemCount() {
        int total = 0;
        for (BasketProduct p : products.values()) {
            total += p.quantity;
        }
        return total;
    }

    public void clear() {
        products.clear();
    }

    private static class BasketProduct {

        private Product product;
        private int quantity;

        BasketProduct(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }

        public Product getProduct() {
            return product;
        }

        public void setProduct(Product product) {
            this.product = product;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}
