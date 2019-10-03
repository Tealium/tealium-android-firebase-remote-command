package com.tealium.example.model;

public class Product {

    private String mProductId;
    private String mProductName;
    private String mProductCategory;
    private Integer mProductQuantity;
    private Long mProductUnitPrice;

    public Product(String productId, String productName, String productCategory, Integer productQuantity, Long productUnitPrice) {
        mProductId = productId;
        mProductName = productName;
        mProductCategory = productCategory;
        mProductQuantity = productQuantity;
        mProductUnitPrice = productUnitPrice;
    }

    public String getProductId() {
        return mProductId;
    }

    public void setProductId(String productId) {
        mProductId = productId;
    }

    public String getProductName() {
        return mProductName;
    }

    public void setProductName(String productName) {
        mProductName = productName;
    }

    public String getProductCategory() {
        return mProductCategory;
    }

    public void setProductCategory(String productCategory) {
        mProductCategory = productCategory;
    }

    public Integer getProductQuantity() {
        return mProductQuantity;
    }

    public void setProductQuantity(Integer productQuantity) {
        mProductQuantity = productQuantity;
    }

    public Long getProductUnitPrice() {
        return mProductUnitPrice;
    }

    public void setProductUnitPrice(Long productUnitPrice) {
        mProductUnitPrice = productUnitPrice;
    }
}
