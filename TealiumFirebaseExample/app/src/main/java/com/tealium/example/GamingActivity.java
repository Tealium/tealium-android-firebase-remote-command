package com.tealium.example;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tealium.example.helper.DataLayer;
import com.tealium.example.helper.TealiumHelper;
import com.tealium.example.model.Player;

import java.util.HashMap;
import java.util.Map;

public class GamingActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String DEFAULT_CHARACTER_NAME = "The Beast";

    EditText mCharacterNameEditText;
    Button mLevelUpButton;
    Button mTutorialBeginButton;
    Button mTutorialEndButton;
    Button mUnlockAchievementButton;
    Button mEarnVirtualCurrencyButton;
    Button mSpendVirtualCurrencyButton;

    private Player mPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gaming);

        mCharacterNameEditText = findViewById(R.id.edit_text_games_character_name);

        mLevelUpButton = findViewById(R.id.btn_games_level_up);
        mTutorialBeginButton = findViewById(R.id.btn_games_tutorial_begin);
        mTutorialEndButton = findViewById(R.id.btn_games_tutorial_end);
        mUnlockAchievementButton = findViewById(R.id.btn_games_unlock_achievement);
        mEarnVirtualCurrencyButton = findViewById(R.id.btn_games_earn_virtual_currency);
        mSpendVirtualCurrencyButton = findViewById(R.id.btn_games_spend_virtual_currency);

        mLevelUpButton.setOnClickListener(this);
        mTutorialBeginButton.setOnClickListener(this);
        mTutorialEndButton.setOnClickListener(this);
        mUnlockAchievementButton.setOnClickListener(this);
        mEarnVirtualCurrencyButton.setOnClickListener(this);
        mSpendVirtualCurrencyButton.setOnClickListener(this);

        mPlayer = new Player(DEFAULT_CHARACTER_NAME);
        showMessage("You have " + mPlayer.getCurrentVirtualCurrency() + " Beast Coin remaining.");

        TealiumHelper.trackScreen(this, "Gaming Page");
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.btn_games_level_up:
                String charName = mCharacterNameEditText.getText().toString();
                if (!mPlayer.getPlayerName().equals(charName)) {
                    mPlayer.setPlayerName(charName);
                }
                mPlayer.levelUp(0);

                showMessage(mPlayer.getPlayerName() + " has reached level: " + mPlayer.getCurrenLevel());
                break;
            case R.id.btn_games_tutorial_begin:
                mPlayer.startTutorial();
                break;
            case R.id.btn_games_tutorial_end:
                mPlayer.stopTutorial();
                break;
            case R.id.btn_games_unlock_achievement:
                mPlayer.unlockAchievement("ACH-ID-1234");
                showMessage("Achievement Unlocked!");
                break;
            case R.id.btn_games_earn_virtual_currency:
                mPlayer.earnVirtualCurrency();
                showMessage("You have " + mPlayer.getCurrentVirtualCurrency() + " available. Why not spend some?");
                break;
            case R.id.btn_games_spend_virtual_currency:
                if (mPlayer.canSpend(10)) {
                    showMessage("You don't have enough Beast Coin. Earn some more first.");
                    return;
                }
                mPlayer.spendVirtualCurrency();
                showMessage("You have " + mPlayer.getCurrentVirtualCurrency() + " Beast Coin remaining.");
                break;
        }
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


}
