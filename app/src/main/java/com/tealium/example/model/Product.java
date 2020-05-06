package com.tealium.example.model;

import androidx.annotation.Nullable;

public class Product {

    private String mProductId;
    private String mProductName;
    private String mProductCategory;
    private String mProductVariant;
    private String mProductBrand;
    private Double mProductUnitPrice;

    public Product(String productId, String productName, String productCategory, String productVariant, String productBrand, Double productUnitPrice) {
        mProductId = productId;
        mProductName = productName;
        mProductCategory = productCategory;
        mProductVariant = productVariant;
        mProductBrand = productBrand;
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

    public String getProductVariant() {
        return mProductVariant;
    }

    public void setProductVariant(String productVariant) {
        this.mProductVariant = productVariant;
    }

    public String getProductBrand() {
        return mProductBrand;
    }

    public void setProductBrand(String productBrand) {
        this.mProductBrand = productBrand;
    }

    public Double getProductUnitPrice() {
        return mProductUnitPrice;
    }

    public void setProductUnitPrice(Double productUnitPrice) {
        mProductUnitPrice = productUnitPrice;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + mProductId.hashCode();
        result = 31 * result + mProductName.hashCode();
        result = 31 * result + mProductCategory.hashCode();
        result = 31 * result + mProductVariant.hashCode();
        result = 31 * result + mProductBrand.hashCode();
        long unitPriceLong = Double.doubleToLongBits(mProductUnitPrice);
        result = 31 * result + (int) (unitPriceLong ^ (unitPriceLong >>> 32));
        return result;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Product)) {
            return false;
        }
        Product p = (Product) obj;
        return mProductId.equals(p.getProductId())
                && mProductName.equals(p.getProductName())
                && mProductCategory.equals(p.getProductCategory())
                && mProductVariant.equals(p.getProductVariant())
                && mProductBrand.equals(p.getProductBrand())
                && mProductUnitPrice.equals(p.getProductUnitPrice());
    }
}
