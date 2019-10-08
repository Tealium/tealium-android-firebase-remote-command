package com.tealium.remotecommands.firebase;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.tealium.internal.tagbridge.RemoteCommand;

import static com.tealium.remotecommands.firebase.FirebaseConstants.TAG;
import static com.tealium.remotecommands.firebase.FirebaseConstants.Commands;
import static com.tealium.remotecommands.firebase.FirebaseConstants.Keys;

import org.json.JSONObject;

public class FirebaseRemoteCommand extends RemoteCommand {


    FirebaseTrackable mFirebaseTrackable;
    private static Activity mCurrentActivity;

    public static final String DEFAULT_COMMAND_ID = "firebaseAnalytics";
    public static final String DEFAULT_COMMAND_DESCRIPTION = "Tealium Firebase Analytics integration";
    static final int sErrorTime = -1;
    static final boolean sDefaultAnalyticsEnabled = true;

    /**
     * Default constructor uses {@value DEFAULT_COMMAND_ID} and {@value DEFAULT_COMMAND_DESCRIPTION}
     * for the command id and description.
     * The command id will need to be reflected in your Tag in Tealium IQ.
     *
     * @param application Instance of the Application
     */
    public FirebaseRemoteCommand(Application application) {
        this(application, DEFAULT_COMMAND_ID, DEFAULT_COMMAND_DESCRIPTION);
    }

    /**
     * Will default to use {@value DEFAULT_COMMAND_ID} and {@value DEFAULT_COMMAND_DESCRIPTION}
     * for the command id and description if null values are supplied.
     * The command id will need to be reflected in your Tag in Tealium IQ.
     *
     * @param application Instance of the Application
     * @param commandId   Override for the commandId used in Tealium IQ
     * @param description Override the default description
     */
    public FirebaseRemoteCommand(Application application, String commandId, String description) {
        super(commandId != null ? commandId : DEFAULT_COMMAND_ID,
                description != null ? description : DEFAULT_COMMAND_DESCRIPTION);

        Application.ActivityLifecycleCallbacks cb = createActivityLifecycleCallbacks();
        application.registerActivityLifecycleCallbacks(cb);

        mFirebaseTrackable = new FirebaseTracker(application.getApplicationContext());
    }

    /**
     * @param response
     */
    @Override
    protected void onInvoke(RemoteCommand.Response response) {
        JSONObject payload = response.getRequestPayload();
        String[] commandArray = splitCommands(payload);
        parseCommands(commandArray, payload);

        response.send();
    }

    private String[] splitCommands(JSONObject payload) {
        String commandString = payload.optString(Keys.COMMAND_NAME, "");
        return commandString.split(FirebaseConstants.SEPARATOR);
    }

    private void parseCommands(String[] commandList, JSONObject payload) {
        for (String command : commandList) {
            command = command.trim();
            try {
                switch (command) {
                    case Commands.CONFIGURE:
                        mFirebaseTrackable.configure(
                                payload.optInt(Keys.SESSION_TIMEOUT, sErrorTime) * 1000,
                                payload.optInt(Keys.MIN_SECONDS, sErrorTime) * 1000,
                                payload.optBoolean(Keys.ANALYTICS_ENABLED, sDefaultAnalyticsEnabled));
                        break;
                    case Commands.LOG_EVENT:
                        String eventName = payload.optString(Keys.EVENT_NAME, null);
                        JSONObject params = payload.optJSONObject(Keys.EVENT_PARAMS);
                        mFirebaseTrackable.logEvent(eventName, params);
                        break;
                    case Commands.SET_SCREEN_NAME:
                        String screenName = payload.optString(Keys.SCREEN_NAME, null);
                        String screenClass = payload.optString(Keys.SCREEN_CLASS, null);
                        mFirebaseTrackable.setScreenName(mCurrentActivity, screenName, screenClass);
                        break;
                    case Commands.SET_USER_PROPERTY:
                        String propertyName = payload.optString(Keys.USER_PROPERTY_NAME, null);
                        String propertyValue = payload.optString(Keys.USER_PROPERTY_VALUE, null);
                        mFirebaseTrackable.setUserProperty(propertyName, propertyValue);
                        break;
                    case Commands.SET_USER_ID:
                        String userId = payload.optString(Keys.USER_ID, null);
                        mFirebaseTrackable.setUserId(userId);
                        break;
                    case Commands.RESET_DATA:
                        mFirebaseTrackable.resetData();
                        break;
                }
            } catch (Exception ex) {
                Log.w(TAG, "Error processing command: " + command, ex);
            }
        }
    }


    // Setup lifecycle callbacks to init FirebaseAnalytics. You may prefer to do this manually.
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private static Application.ActivityLifecycleCallbacks createActivityLifecycleCallbacks() {

        return new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                mCurrentActivity = activity;
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        };
    }

    /**
     * Helper method to determine whether a particular key in a JSONObject is present AND also is
     * not a null value.
     *
     * @param json - the JSONObject to inspect
     * @param key  - the key in the JSONObject to check existence and value
     * @return
     */
    static boolean keyHasValue(JSONObject json, String key) {
        return (json != null && json.has(key) && !json.isNull(key));
    }

}
