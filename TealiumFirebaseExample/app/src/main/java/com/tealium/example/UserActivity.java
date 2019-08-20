package com.tealium.example;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tealium.example.helper.DataLayer;
import com.tealium.example.helper.TealiumHelper;
import com.tealium.remotecommands.firebase.FirebaseConstants;

import java.util.HashMap;
import java.util.Map;

public class UserActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = UserActivity.class.getSimpleName();

    private Button mSetUserIdButton;
    private EditText mUserIdEditText;
    private EditText mUserPropertyValueEditText;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        mUserIdEditText = findViewById(R.id.edit_text_user_id);
        mUserPropertyValueEditText = findViewById(R.id.edit_text_user_property_value);

        mSetUserIdButton = findViewById(R.id.btn_set_user_id);
        mSetUserIdButton.setOnClickListener(this);

        TealiumHelper.trackScreen(this, "User Details");
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_set_user_id) {
            setUserid();
        }
    }

    private void setUserid() {
        Map<String, Object> data = new HashMap<>();
        data.put(DataLayer.CUSTOMER_ID, mUserIdEditText.getText().toString());
        data.put(DataLayer.USERNAME, mUserPropertyValueEditText.getText().toString());

        TealiumHelper.trackEvent("user_login", data);
    }
}
