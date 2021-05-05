package com.tealium.example.model;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.tealium.example.helper.DataLayer;
import com.tealium.example.helper.TealiumHelper;

import java.util.HashMap;
import java.util.Map;

public class Player {

    public static final String CURRENCY = "Beast-Coin";
    private static final int sBeastCoinIncrement = 10;

    private String mName;
    private int mCurrentBeastCoin;
    private int mCurrentLevel;
    private boolean mIsInTutorial;

    public Player(String mName) {
        this.mName = mName;
        mCurrentBeastCoin = 0;
        mCurrentLevel = 0;
        mIsInTutorial = false;
    }

    public String getPlayerName() {
        return mName;
    }

    public void setPlayerName(String name) {
        if (name != null && !name.equals("")) {
            mName = name;
        }
    }

    public void earnVirtualCurrency() {
        earnVirtualCurrency(sBeastCoinIncrement);
    }

    public void earnVirtualCurrency(int amount) {
        mCurrentBeastCoin += amount;

        Map<String, Object> data = new HashMap<>();
        data.put(DataLayer.NUMBER_OF_TOKENS, amount);
        data.put(DataLayer.CURRENCY_TYPE, CURRENCY);
        TealiumHelper.trackEvent(FirebaseAnalytics.Event.EARN_VIRTUAL_CURRENCY, data);
    }

    public void spendVirtualCurrency() {
        spendVirtualCurrency(sBeastCoinIncrement);
    }

    public void spendVirtualCurrency(int amount) {
        if (canSpend(amount)) {
            return;
        }
        mCurrentBeastCoin -= amount;

        Map<String, Object> data = new HashMap<>();
        data.put(DataLayer.PRODUCT_NAME, "Beast Shield");
        data.put(DataLayer.CURRENCY_TYPE, CURRENCY);
        data.put(DataLayer.NUMBER_OF_TOKENS, amount);
        TealiumHelper.trackEvent(FirebaseAnalytics.Event.SPEND_VIRTUAL_CURRENCY, data);
    }

    public boolean canSpend(int amount) {
        return mCurrentBeastCoin <= 0 || mCurrentBeastCoin - amount < 0;
    }

    public int getCurrentVirtualCurrency() {
        return mCurrentBeastCoin;
    }

    public void levelUp(int newLevel) {
        if (newLevel > mCurrentBeastCoin) {
            mCurrentLevel = newLevel;
        } else {
            mCurrentLevel++;
        }

        Map<String, Object> data = new HashMap<>();
        data.put(DataLayer.LEVEL, mCurrentLevel);
        data.put(DataLayer.CHARACTER, mName);
        TealiumHelper.trackEvent(FirebaseAnalytics.Event.LEVEL_UP, data);
    }

    public int getCurrenLevel() {
        return mCurrentLevel;
    }

    public void unlockAchievement(String achievementId) {
        Map<String, Object> data = new HashMap<>();
        data.put(DataLayer.ACHIEVEMENT_ID, achievementId);
        TealiumHelper.trackEvent(FirebaseAnalytics.Event.UNLOCK_ACHIEVEMENT, data);
    }

    public void startTutorial() {
        mIsInTutorial = true;
        TealiumHelper.trackEvent(FirebaseAnalytics.Event.TUTORIAL_BEGIN, null);
    }

    public void stopTutorial() {
        mIsInTutorial = false;
        TealiumHelper.trackEvent(FirebaseAnalytics.Event.TUTORIAL_COMPLETE, null);
    }

    public boolean isInTutorial() {
        return mIsInTutorial;
    }
}
