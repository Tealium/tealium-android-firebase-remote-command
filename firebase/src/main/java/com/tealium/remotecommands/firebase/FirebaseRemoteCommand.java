package com.tealium.remotecommands.firebase;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.tealium.remotecommands.RemoteCommand;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import javax.annotation.Nullable;

public class FirebaseRemoteCommand extends RemoteCommand {

    FirebaseCommand mFirebaseCommand;
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
                description != null ? description : DEFAULT_COMMAND_DESCRIPTION,
                BuildConfig.TEALIUM_FIREBASE_VERSION);

        Application.ActivityLifecycleCallbacks cb = createActivityLifecycleCallbacks();
        application.registerActivityLifecycleCallbacks(cb);

        mFirebaseCommand = new FirebaseInstance(application.getApplicationContext());
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
        String commandString = payload.optString(FirebaseConstants.Keys.COMMAND_NAME, "");
        return commandString.split(FirebaseConstants.SEPARATOR);
    }

    private void parseCommands(String[] commandList, JSONObject payload) {
        for (String command : commandList) {
            command = command.trim().toLowerCase();
            try {
                Log.i(FirebaseConstants.TAG, "Processing command: " + command + " with payload: " + payload.toString());
                switch (command) {
                    case FirebaseConstants.Commands.CONFIGURE:
                        mFirebaseCommand.configure(
                                payload.optInt(FirebaseConstants.Keys.SESSION_TIMEOUT, sErrorTime) * 1000,
                                payload.optBoolean(FirebaseConstants.Keys.ANALYTICS_ENABLED, sDefaultAnalyticsEnabled));
                        break;
                    case FirebaseConstants.Commands.LOG_EVENT:
                        String eventName = payload.optString(FirebaseConstants.Keys.EVENT_NAME, null);
                        JSONObject params = getParams(payload, FirebaseConstants.Keys.EVENT_PARAMS, FirebaseConstants.Keys.TAG_EVENT_PARAMS);
                        JSONObject items = payload.optJSONObject(FirebaseConstants.Keys.ITEMS_PARAMS);
                        if (items != null) {
                            params.put("param_items", itemsParamsToJsonArray(items));
                        }
                        mFirebaseCommand.logEvent(eventName, params);
                        break;
                    case FirebaseConstants.Commands.SET_SCREEN_NAME:
                        String screenName = payload.optString(FirebaseConstants.Keys.SCREEN_NAME, null);
                        String screenClass = payload.optString(FirebaseConstants.Keys.SCREEN_CLASS, null);
                        mFirebaseCommand.setScreenName(mCurrentActivity, screenName, screenClass);
                        break;
                    case FirebaseConstants.Commands.SET_USER_PROPERTY:
                        String propertyName = payload.optString(FirebaseConstants.Keys.USER_PROPERTY_NAME, null);
                        String propertyValue = payload.optString(FirebaseConstants.Keys.USER_PROPERTY_VALUE, null);
                        mFirebaseCommand.setUserProperty(propertyName, propertyValue);
                        break;
                    case FirebaseConstants.Commands.SET_USER_ID:
                        String userId = payload.optString(FirebaseConstants.Keys.USER_ID, null);
                        mFirebaseCommand.setUserId(userId);
                        break;
                    case FirebaseConstants.Commands.RESET_DATA:
                        mFirebaseCommand.resetData();
                        break;
                    case FirebaseConstants.Commands.SET_DEFAULT_PARAMETERS:
                        JSONObject defaultParams = getParams(payload, FirebaseConstants.Keys.DEFAULT_PARAMS, FirebaseConstants.Keys.TAG_DEFAULT_PARAMS);
                        mFirebaseCommand.setDefaultEventParameters(defaultParams);
                        break;
                    case FirebaseConstants.Commands.SET_CONSENT:
                        JSONObject consentParams = getParams(payload, FirebaseConstants.Keys.CONSENT_SETTINGS, null);
                        mFirebaseCommand.setConsent(consentParams);
                        break;
                }
            } catch (Exception ex) {
                Log.w(FirebaseConstants.TAG, "Error processing command: " + command, ex);
            }
        }
    }

    private static JSONObject getParams(JSONObject payload, String key, @Nullable String fallbackKey) {
        JSONObject params = payload.optJSONObject(key);
        if (params == null) {
            if (fallbackKey != null) {
                params = payload.optJSONObject(fallbackKey);
            }
            if (params == null) {
                params = new JSONObject();
            }
        }
        return params;
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
                mCurrentActivity = activity;
            }

            @Override
            public void onActivityResumed(Activity activity) {
                mCurrentActivity = activity;
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

    private JSONArray itemsParamsToJsonArray(JSONObject itemsParam) {
        try {
            if (itemsParam.get(FirebaseConstants.ItemProperties.ID) instanceof JSONArray) {
                // split array of items
                return formatItems(itemsParam, itemsParam.getJSONArray(FirebaseConstants.ItemProperties.ID).length());
            } else {
                // format single item
                return formatItems(itemsParam, 0);
            }
        } catch (JSONException e) {
            Log.d(FirebaseConstants.TAG, "Error formatting items param: " + e.toString());
        }

        return new JSONArray();
    }

    private JSONArray formatItems(JSONObject json, int numItems) {
        JSONArray res = new JSONArray();
        try {
            if (numItems > 0) {
                for (int i = 0; i < numItems; i++) {
                    JSONObject item = new JSONObject();
                    if (json.optJSONArray(FirebaseConstants.ItemProperties.ID) != null) {
                        item.put(FirebaseConstants.ItemProperties.ID, json.getJSONArray(FirebaseConstants.ItemProperties.ID).get(i));
                    }
                    if (json.optJSONArray(FirebaseConstants.ItemProperties.BRAND) != null) {
                        item.put(FirebaseConstants.ItemProperties.BRAND, json.getJSONArray(FirebaseConstants.ItemProperties.BRAND).get(i));
                    }
                    if (json.optJSONArray(FirebaseConstants.ItemProperties.CATEGORY) != null) {
                        item.put(FirebaseConstants.ItemProperties.CATEGORY, json.getJSONArray(FirebaseConstants.ItemProperties.CATEGORY).get(i));
                    }
                    if (json.optJSONArray(FirebaseConstants.ItemProperties.NAME) != null) {
                        item.put(FirebaseConstants.ItemProperties.NAME, json.getJSONArray(FirebaseConstants.ItemProperties.NAME).get(i));
                    }
                    if (json.optJSONArray(FirebaseConstants.ItemProperties.PRICE) != null) {
                        item.put(FirebaseConstants.ItemProperties.PRICE, json.getJSONArray(FirebaseConstants.ItemProperties.PRICE).get(i));
                    }
                    if (json.optJSONArray(FirebaseConstants.ItemProperties.QUANTITY) != null) {
                        item.put(FirebaseConstants.ItemProperties.QUANTITY, json.getJSONArray(FirebaseConstants.ItemProperties.QUANTITY).get(i));
                    }
                    if (json.optJSONArray(FirebaseConstants.ItemProperties.INDEX) != null) {
                        item.put(FirebaseConstants.ItemProperties.INDEX, json.getJSONArray(FirebaseConstants.ItemProperties.INDEX).get(i));
                    }
                    if (json.optJSONArray(FirebaseConstants.ItemProperties.LIST) != null) {
                        item.put(FirebaseConstants.ItemProperties.LIST, json.getJSONArray(FirebaseConstants.ItemProperties.LIST).get(i));
                    }
                    if (json.optJSONArray(FirebaseConstants.ItemProperties.LOCATION_ID) != null) {
                        item.put(FirebaseConstants.ItemProperties.LOCATION_ID, json.getJSONArray(FirebaseConstants.ItemProperties.LOCATION_ID).get(i));
                    }
                    if (json.optJSONArray(FirebaseConstants.ItemProperties.VARIANT) != null) {
                        item.put(FirebaseConstants.ItemProperties.VARIANT, json.getJSONArray(FirebaseConstants.ItemProperties.VARIANT).get(i));
                    }

                    res.put(item);
                }
            } else {
                JSONObject item = new JSONObject();
                Iterator iter = json.keys();
                while (iter.hasNext()) {
                    String key = (String) iter.next();
                    switch (key) {
                        case FirebaseConstants.ItemProperties.ID:
                        case FirebaseConstants.ItemProperties.BRAND:
                        case FirebaseConstants.ItemProperties.CATEGORY:
                        case FirebaseConstants.ItemProperties.NAME:
                        case FirebaseConstants.ItemProperties.PRICE:
                        case FirebaseConstants.ItemProperties.QUANTITY:
                        case FirebaseConstants.ItemProperties.INDEX:
                        case FirebaseConstants.ItemProperties.LIST:
                        case FirebaseConstants.ItemProperties.LOCATION_ID:
                        case FirebaseConstants.ItemProperties.VARIANT:
                            item.put(key, json.get(key));
                            break;
                        default:
                            Log.d(FirebaseConstants.TAG, "Invalid item param key: " + key + ".");
                            break;
                    }
                }
                res.put(item);
            }

        } catch (JSONException e) {
            Log.d(FirebaseConstants.TAG, "Error formatting items param: " + e.toString());
        }
        return res;
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
