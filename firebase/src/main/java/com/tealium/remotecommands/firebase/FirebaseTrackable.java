package com.tealium.remotecommands.firebase;

import android.app.Activity;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONObject;

interface FirebaseTrackable {

    /**
     * Configures the Timeout, and Minimum Session Duration as well as whether to enable Analytics.
     *
     * @param timeout
     * @param minSeconds
     * @param analyticsEnabled
     */
    void configure(Integer timeout, Integer minSeconds, Boolean analyticsEnabled);

    /**
     * Logs an event for the given eventName and eventParams.
     *
     * @param eventName
     * @param eventParams
     */
    void logEvent(String eventName, JSONObject eventParams);

    /**
     * Sets the Screen Name and Class for the current Activity.
     *
     * @param currentActivity
     * @param screenName
     * @param screenClass
     */
    void setScreenName(Activity currentActivity, String screenName, String screenClass);

    /**
     * Sets the User Identifier for the given userId.
     *
     * @param userId
     */
    void setUserId(String userId);

    /**
     * Sets a User property for the given propertyName and propertyValue.
     *
     * @param propertyName
     * @param propertyValue
     */
    void setUserProperty(String propertyName, String propertyValue);

    /**
     * Calls {@link FirebaseAnalytics#resetAnalyticsData()} to reset all known analytics data.
     */
    void resetData();

}
