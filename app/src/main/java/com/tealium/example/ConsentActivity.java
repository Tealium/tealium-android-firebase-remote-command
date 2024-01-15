package com.tealium.example;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.tealium.example.helper.DataLayer;
import com.tealium.example.helper.TealiumHelper;
import com.tealium.example.model.Basket;
import com.tealium.example.model.Product;
import com.tealium.example.model.Store;

import java.util.HashMap;
import java.util.Map;

public class ConsentActivity extends AppCompatActivity {

    Switch mAnalyticsStorage;
    Switch mAdStorage;
    Switch mAdPersonalization;
    Switch mAdUserData;
    Button mSetConsentButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consent);

        mAnalyticsStorage = this.<Switch>findViewById(R.id.sw_analytics_storage);
        mAdStorage = this.<Switch>findViewById(R.id.sw_ad_storage);
        mAdPersonalization = this.<Switch>findViewById(R.id.sw_ad_personalization);
        mAdUserData = this.<Switch>findViewById(R.id.sw_ad_user_data);
        mSetConsentButton = findViewById(R.id.set_consent);

        mSetConsentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> data = new HashMap<>();
                data.put("consent_analytics_storage", mAnalyticsStorage.isChecked() ? "granted" : "denied");
                data.put("consent_ad_storage", mAdStorage.isChecked() ? "granted" : "denied");
                data.put("consent_ad_personalization", mAdPersonalization.isChecked() ? "granted" : "denied");
                data.put("consent_ad_user_data", mAdUserData.isChecked() ? "granted" : "denied");
                TealiumHelper.trackEvent("setconsent", data);
            }
        });
    }
}
