package com.tealium.example;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tealium.example.helper.DataLayer;
import com.tealium.example.helper.TealiumHelper;

import java.util.HashMap;
import java.util.Map;

public class EcommerceActivity extends AppCompatActivity implements View.OnClickListener {

    Button mCategoryViewButton;
    Button mProductViewButton;
    Button mCartAddButton;
    Button mCheckoutButton;
    Button mPurchaseButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecommerce);

        mCategoryViewButton = findViewById(R.id.btn_ecom_category_view);
        mProductViewButton = findViewById(R.id.btn_ecom_product_view);
        mCartAddButton = findViewById(R.id.btn_ecom_cart_add);
        mCheckoutButton = findViewById(R.id.btn_ecom_checkout);
        mPurchaseButton = findViewById(R.id.btn_ecom_purchase);

        mCategoryViewButton.setOnClickListener(this);
        mProductViewButton.setOnClickListener(this);
        mCartAddButton.setOnClickListener(this);
        mCheckoutButton.setOnClickListener(this);
        mPurchaseButton.setOnClickListener(this);

        TealiumHelper.trackScreen(this, "Shop");
    }

    @Override
    public void onClick(View view) {
        final int id = view.getId();
        Map<String, Object> data = new HashMap<>();

        switch (id) {
            case R.id.btn_ecom_category_view:
                data.put(DataLayer.PRODUCT_CATEGORY, "Category 1");
                TealiumHelper.trackView("category", data);
                break;
            case R.id.btn_ecom_product_view:
                data.put(DataLayer.PRODUCT_ID, "SKU123");
                data.put(DataLayer.PRODUCT_NAME, "Product 123");
                data.put(DataLayer.PRODUCT_CATEGORY, "Category 1");
                TealiumHelper.trackView("product", data);
                break;
            case R.id.btn_ecom_cart_add:
                data.put(DataLayer.PRODUCT_ID, "SKU123");
                data.put(DataLayer.PRODUCT_NAME, "Product 123");
                data.put(DataLayer.PRODUCT_CATEGORY, "Category 1");
                data.put(DataLayer.PRODUCT_QUANTITY, 1);
                data.put(DataLayer.PRODUCT_PRICE, 10.00);
                TealiumHelper.trackEvent("cart_add", data);
                break;
            case R.id.btn_ecom_checkout:
                TealiumHelper.trackEvent("checkout", data);
                break;
            case R.id.btn_ecom_purchase:
                data.put(DataLayer.PRODUCT_ID, "SKU123");
                data.put(DataLayer.PRODUCT_NAME, "Product 123");
                data.put(DataLayer.PRODUCT_CATEGORY, "Category 1");
                data.put(DataLayer.PRODUCT_QUANTITY, 1);
                data.put(DataLayer.PRODUCT_PRICE, 10.00);

                data.put(DataLayer.ORDER_CURRENCY, "USD");
                data.put(DataLayer.ORDER_TOTAL, 10.00);
                data.put(DataLayer.ORDER_ID, "ORD-12345");
                TealiumHelper.trackEvent("order", data);
                break;
        }
    }
}
