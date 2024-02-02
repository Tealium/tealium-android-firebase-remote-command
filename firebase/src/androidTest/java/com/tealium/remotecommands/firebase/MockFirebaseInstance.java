package com.tealium.remotecommands.firebase;

import android.app.Activity;
import android.content.Context;

import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class MockFirebaseInstance extends FirebaseInstance {

    public MockFirebaseInstance(Context applicationContext) {
        super(applicationContext);
    }

    public List<String> methodsCalled = new LinkedList<>();

    private void addCallerName() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        for (StackTraceElement stackTraceElement : stackTraceElements) {
            if (stackTraceElement.getClassName().contains("com.tealium") && !stackTraceElement.getMethodName().contains("addCallerName")) {
                methodsCalled.add(stackTraceElement.getMethodName());
                break;
            }
        }
    }

    @Override
    public void configure(Integer timeout, Boolean analyticsEnabled) {
        super.configure(timeout, analyticsEnabled);
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

    @Override
    public void setDefaultEventParameters(JSONObject parameters) {
        super.setDefaultEventParameters(parameters);
        addCallerName();
    }

    @Override
    public void setConsent(JSONObject parameters) {
        super.setConsent(parameters);
        addCallerName();
    }
}
