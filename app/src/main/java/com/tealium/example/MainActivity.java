package com.tealium.example;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tealium.example.helper.DataLayer;
import com.tealium.example.helper.TealiumHelper;
import com.tealium.remotecommands.firebase.FirebaseConstants;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button mLaunchUserDetailsButton;
    Button mLaunchEcommerceButton;
    Button mLaunchGamingButton;
    Button mLaunchTravelButton;
    Button mResetUserDataButton;
    Button mSearchButton;
    EditText mSearchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLaunchUserDetailsButton = findViewById(R.id.btn_launch_user_details);
        mLaunchUserDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, UserActivity.class));
            }
        });

        mLaunchEcommerceButton = findViewById(R.id.btn_launch_ecommerce);
        mLaunchEcommerceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, EcommerceActivity.class));
            }
        });

        mLaunchGamingButton = findViewById(R.id.btn_launch_gaming);
        mLaunchGamingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, GamingActivity.class));
            }
        });

        mLaunchTravelButton = findViewById(R.id.btn_launch_travel);
        mLaunchTravelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, TravelActivity.class));
            }
        });

        mResetUserDataButton = findViewById(R.id.btn_reset_data);
        mResetUserDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> data = new HashMap<>();
                data.put(FirebaseConstants.Keys.COMMAND_NAME, FirebaseConstants.Commands.RESET_DATA);
                TealiumHelper.trackEvent("reset_data", data);
            }
        });

        mSearchText = findViewById(R.id.edit_text_search_term);
        mSearchButton = findViewById(R.id.btn_search);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String term = mSearchText.getText().toString();
                if (!term.isEmpty()) {
                    Map<String, Object> data = new HashMap<>();
                    data.put(FirebaseConstants.Keys.COMMAND_NAME, FirebaseConstants.Commands.SET_DEFAULT_PARAMETERS);
                    data.put(DataLayer.SEARCH_KEYWORD, term);
                    TealiumHelper.trackEvent("search", data);
                }
            }
        });

        TealiumHelper.trackScreen(this, "Home Page");
    }
}
