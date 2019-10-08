package com.tealium.remotecommands.firebase;

import android.app.Activity;
import android.content.Context;

import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class MockFirebaseTracker extends FirebaseTracker {

    public MockFirebaseTracker(Context applicationContext) {
        super(applicationContext);
    }

    public List<String> methodsCalled = new LinkedList<>();

    private void addCallerName() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        for (int i = 0; i < stackTraceElements.length; i++) {
            if (stackTraceElements[i].getClassName().contains("com.tealium") && !stackTraceElements[i].getMethodName().contains("addCallerName")) {
                methodsCalled.add(stackTraceElements[i].getMethodName());
                break;
            }
        }
    }

    @Override
    public void configure(Integer timeout, Integer minSeconds, Boolean analyticsEnabled) {
        super.configure(timeout, minSeconds, analyticsEnabled);
        addCallerName();
    }

    @Override
    public void logEvent(String eventName, JSONObject eventParams) {
        super.logEvent(eventName, eventParams);
        addCallerName();
    }

    @Override
    public void setScreenName(Activity currentActivity, String screenName, String screenClass) {
        super.setScreenName(currentActivity, screenName, screenClass);
        addCallerName();
    }

    @Override
    public void setUserId(String userId) {
        super.setUserId(userId);
        addCallerName();
    }

    @Override
    public void setUserProperty(String propertyName, String propertyValue) {
        super.setUserProperty(propertyName, propertyValue);
        addCallerName();
    }

    @Override
    public void resetData() {
        super.resetData();
        addCallerName();
    }
}
