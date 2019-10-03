package com.tealium.example;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tealium.example.helper.DataLayer;
import com.tealium.example.helper.TealiumHelper;

import java.util.HashMap;
import java.util.Map;

public class TravelActivity extends AppCompatActivity implements View.OnClickListener {

    Button mPurchaseButton;
    EditText mTravelOriginText;
    EditText mTravelDestinationText;
    EditText mDateFromText;
    EditText mDateToText;
    EditText mTravelClassText;
    EditText mNumberOfPassengersText;
    EditText mNumberOfRoomsText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);

        mPurchaseButton = findViewById(R.id.btn_travel_purchase);
        mTravelOriginText = findViewById(R.id.edit_text_travel_origin);
        mTravelDestinationText = findViewById(R.id.edit_text_travel_destination);
        mDateFromText = findViewById(R.id.edit_text_travel_date_from);
        mDateToText = findViewById(R.id.edit_text_travel_date_to);
        mTravelClassText = findViewById(R.id.edit_text_travel_class);
        mNumberOfPassengersText = findViewById(R.id.edit_text_number_of_passengers);
        mNumberOfRoomsText = findViewById(R.id.edit_text_number_of_rooms);

        mPurchaseButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.btn_travel_purchase:
                Map<String, Object> data = getTravelData();
                data.put(DataLayer.ORDER_CURRENCY, "GBP");
                data.put(DataLayer.ORDER_TOTAL, 100.00);
                data.put(DataLayer.ORDER_ID, "TRAVEL-ORD-12345");
                TealiumHelper.trackEvent("travel_order", data);
                break;
        }
    }

    private Map<String, Object> getTravelData() {
        Map<String, Object> data = new HashMap<>();
        if (getTextBoxValue(mTravelOriginText) != null) {
            data.put(DataLayer.TRAVEL_ORIGIN, getTextBoxValue(mTravelOriginText));
        }
        if (getTextBoxValue(mTravelDestinationText) != null) {
            data.put(DataLayer.TRAVEL_DESTINATION, getTextBoxValue(mTravelDestinationText));
        }
        if (getTextBoxValue(mTravelClassText) != null) {
            data.put(DataLayer.TRAVEL_CLASS, getTextBoxValue(mTravelClassText));
        }
        if (getTextBoxValue(mDateFromText) != null) {
            data.put(DataLayer.TRAVEL_START_DATE, getTextBoxValue(mDateFromText));
        }
        if (getTextBoxValue(mDateToText) != null) {
            data.put(DataLayer.TRAVEL_END_DATE, getTextBoxValue(mDateToText));
        }
        if (getTextBoxValue(mNumberOfPassengersText) != null) {
            data.put(DataLayer.NUMBER_OF_PASSENGERS, getTextBoxValue(mNumberOfPassengersText));
        }
        if (getTextBoxValue(mNumberOfRoomsText) != null) {
            data.put(DataLayer.NUMBER_OF_ROOMS, getTextBoxValue(mNumberOfRoomsText));
        }
        return data;
    }

    private String getTextBoxValue(EditText textbox) {
        String text = textbox.getText().toString();
        return !text.equals("") ? text : null;
    }
}
