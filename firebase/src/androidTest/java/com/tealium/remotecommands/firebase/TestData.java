package com.tealium.remotecommands.firebase;

import android.util.Log;

import com.tealium.remotecommands.RemoteCommand;

import static com.tealium.remotecommands.firebase.FirebaseConstants.SEPARATOR;
import static com.tealium.remotecommands.firebase.FirebaseConstants.TAG;
import static com.tealium.remotecommands.firebase.FirebaseConstants.Commands;
import static com.tealium.remotecommands.firebase.FirebaseConstants.Keys;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TestData {

    public static final class Responses {
        private static final String DEFAULT_RESPONSE_ID = "mock";
        private static final String TEST_TAG = TAG + "-Test";

        private Responses() {
        }

        public static RemoteCommand.Response create(String commandId, String responseId, JSONObject payload) {
            return new RemoteCommand.Response(null, commandId, responseId, payload);
        }

        public static RemoteCommand.Response create(String responseId, JSONObject payload) {
            return create(FirebaseRemoteCommand.DEFAULT_COMMAND_ID, responseId, payload);
        }

        public static RemoteCommand.Response create(JSONObject payload) {
            return create(FirebaseRemoteCommand.DEFAULT_COMMAND_ID, DEFAULT_RESPONSE_ID, payload);
        }

        public static RemoteCommand.Response getValidConfig() {
            JSONObject json = new JSONObject();
            try {
                json.put(Keys.COMMAND_NAME, Commands.CONFIGURE);

                json.put(Keys.ANALYTICS_ENABLED, Values.ANALYTICS_ENABLED_FALSE);
                json.put(Keys.SESSION_TIMEOUT, Values.SESSION_TIMEOUT);
                json.put(Keys.MIN_SECONDS, Values.MIN_SECONDS);
            } catch (JSONException jex) {
                Log.w(TEST_TAG + "-Test", "getValidConfig: ", jex);

            }
            return create(json);
        }

        public static RemoteCommand.Response getInvalidConfig() {
            JSONObject json = new JSONObject();
            try {
                json.put(Keys.COMMAND_NAME, Commands.CONFIGURE);

                json.put(Keys.ANALYTICS_ENABLED, null);
                json.put(Keys.SESSION_TIMEOUT, null);
                json.put(Keys.MIN_SECONDS, null);
            } catch (JSONException jex) {
                Log.w(TEST_TAG + "-Test", "getInvalidConfig: ", jex);
            }
            return create(json);
        }

        public static RemoteCommand.Response getValidEvent() {
            JSONObject json = new JSONObject();
            try {
                json.put(Keys.COMMAND_NAME, Commands.LOG_EVENT);

                json.put(Keys.EVENT_NAME, Values.EVENT_NAME);
                json.put(Keys.EVENT_PARAMS, Values.EVENT_PARAMS);
            } catch (JSONException jex) {
                Log.w(TEST_TAG + "-Test", "getValidEvent: ", jex);
            }
            return create(json);
        }

        public static RemoteCommand.Response getValidTagEvent() throws JSONException {
            JSONObject payload = new JSONObject();
            try {
                payload.put(Keys.COMMAND_NAME, Commands.LOG_EVENT);
                payload.put(Keys.EVENT_NAME, Values.EVENT_NAME);
                payload.put(Keys.TAG_EVENT_PARAMS, Values.EVENT_PARAMS);
            } catch (JSONException jex) {
                Log.w(TEST_TAG + "-Test", "getValidTagEvent: ", jex);
            }

            return create(payload);
        }

        public static RemoteCommand.Response getInvalidEvent() {
            JSONObject json = new JSONObject();
            try {
                json.put(Keys.COMMAND_NAME, Commands.LOG_EVENT);

                json.put(Keys.EVENT_NAME, null);
                json.put(Keys.EVENT_PARAMS, null);
            } catch (JSONException jex) {
                Log.w(TEST_TAG + "-Test", "getInvalidEvent: ", jex);
            }
            return create(json);
        }

        public static RemoteCommand.Response getInvalidTagEvent() throws JSONException {
            JSONObject payload = new JSONObject();
            try {
                payload.put(Keys.COMMAND_NAME, Commands.LOG_EVENT);
                payload.put(Keys.EVENT_NAME, null);
                payload.put(Keys.TAG_EVENT_PARAMS, null);
            } catch (JSONException jex) {
                Log.w(TEST_TAG + "-Test", "getInvalidTagEvent: ", jex);
            }

            return create(payload);
        }

        public static RemoteCommand.Response getValidEcommerceEvent() {
            JSONObject json = new JSONObject();
            try {
                json.put(Keys.COMMAND_NAME, Commands.LOG_EVENT);

                json.put(Keys.EVENT_NAME, Values.EVENT_NAME);
                json.put(Keys.EVENT_PARAMS, Values.ECOMMERCE_PARAMS);
            } catch (JSONException jex) {
                Log.w(TEST_TAG + "-Test", "getValidEvent: ", jex);
            }
            return create(json);
        }

        public static RemoteCommand.Response getInvalidEcommerceEvent() {
            JSONObject json = new JSONObject();
            try {
                json.put(Keys.COMMAND_NAME, Commands.LOG_EVENT);

                json.put(Keys.EVENT_NAME, Values.EVENT_NAME);
                json.put(Keys.EVENT_PARAMS, Values.INVALID_ECOMMERCE_PARAMS);
            } catch (JSONException jex) {
                Log.w(TEST_TAG + "-Test", "getValidEvent: ", jex);
            }
            return create(json);
        }

        public static RemoteCommand.Response getValidScreenName() {
            JSONObject json = new JSONObject();
            try {
                json.put(Keys.COMMAND_NAME, Commands.SET_SCREEN_NAME);

                json.put(Keys.SCREEN_NAME, Values.SCREEN_NAME);
                json.put(Keys.SCREEN_CLASS, Values.SCREEN_CLASS);
            } catch (JSONException jex) {
                Log.w(TEST_TAG + "-Test", "getValidScreenName: ", jex);
            }
            return create(json);
        }

        public static RemoteCommand.Response getInvalidScreenName() {
            JSONObject json = new JSONObject();
            try {
                json.put(Keys.COMMAND_NAME, Commands.SET_SCREEN_NAME);

                json.put(Keys.SCREEN_NAME, null);
                json.put(Keys.SCREEN_CLASS, null);
            } catch (JSONException jex) {
                Log.w(TEST_TAG + "-Test", "getInvalidScreenName: ", jex);
            }
            return create(json);
        }

        public static RemoteCommand.Response getValidUserProperty() {
            JSONObject json = new JSONObject();
            try {
                json.put(Keys.COMMAND_NAME, Commands.SET_USER_PROPERTY);

                json.put(Keys.USER_PROPERTY_NAME, Values.USER_PROPERTY_NAME);
                json.put(Keys.USER_PROPERTY_VALUE, Values.USER_PROPERTY_VALUE);
            } catch (JSONException jex) {
                Log.w(TEST_TAG + "-Test", "getValidUserProperty: ", jex);
            }
            return create(json);
        }

        public static RemoteCommand.Response getInvalidUserProperty() {
            JSONObject json = new JSONObject();
            try {
                json.put(Keys.COMMAND_NAME, Commands.SET_USER_PROPERTY);

                json.put(Keys.USER_PROPERTY_NAME, null);
                json.put(Keys.USER_PROPERTY_VALUE, null);
            } catch (JSONException jex) {
                Log.w(TEST_TAG + "-Test", "getInvalidUserProperty: ", jex);
            }
            return create(json);
        }

        public static RemoteCommand.Response getValidUserId() {
            JSONObject json = new JSONObject();
            try {
                json.put(Keys.COMMAND_NAME, Commands.SET_USER_ID);

                json.put(Keys.USER_ID, Values.USER_ID);

            } catch (JSONException jex) {
                Log.w(TEST_TAG + "-Test", "getValidUserId: ", jex);
            }
            return create(json);
        }

        public static RemoteCommand.Response getInvalidUserId() {
            JSONObject json = new JSONObject();
            try {
                json.put(Keys.COMMAND_NAME, Commands.SET_USER_ID);

                json.put(Keys.USER_ID, null);

            } catch (JSONException jex) {
                Log.w(TEST_TAG + "-Test", "getInvalidUserId: ", jex);
            }
            return create(json);
        }

        public static RemoteCommand.Response getResetData() {
            JSONObject json = new JSONObject();
            try {
                json.put(Keys.COMMAND_NAME, Commands.RESET_DATA);

            } catch (JSONException jex) {
                Log.w(TEST_TAG + "-Test", "getResetData: ", jex);
            }
            return create(json);
        }

        public static RemoteCommand.Response getCompositeData() {
            JSONObject json = new JSONObject();
            try {
                List<String> commandList = new ArrayList<>();
                commandList.add(Commands.CONFIGURE);
                commandList.add(Commands.LOG_EVENT);
                commandList.add(Commands.SET_SCREEN_NAME);
                commandList.add(Commands.SET_USER_PROPERTY);
                commandList.add(Commands.SET_USER_ID);
                commandList.add(Commands.RESET_DATA);

                json.put(Keys.COMMAND_NAME, String.join(SEPARATOR, commandList));

            } catch (JSONException jex) {
                Log.w(TEST_TAG + "-Test", "getResetData: ", jex);
            }
            return create(json);
        }
    }

    public static final class Values {
        private Values() {
        }

        public static final Integer SESSION_TIMEOUT = 36000;
        public static final Integer MIN_SECONDS = 3600;
        public static final Boolean ANALYTICS_ENABLED_TRUE = true;
        public static final Boolean ANALYTICS_ENABLED_FALSE = false;

        public static final String EVENT_NAME = "TestEvent";
        public static final JSONObject EVENT_PARAMS = new JSONObject();
        public static final JSONObject ECOMMERCE_PARAMS = new JSONObject();
        public static final JSONObject INVALID_ECOMMERCE_PARAMS = new JSONObject();
        public static final JSONArray ITEMS_ARRAY = new JSONArray();
        public static final JSONObject ITEMS_PARAMS = new JSONObject();

        public static final String SCREEN_NAME = "TestScreenName";
        public static final String SCREEN_CLASS = "TestScreenClass";

        public static final String USER_PROPERTY_NAME = "TestUserProperty";
        public static final String USER_PROPERTY_VALUE = "TestUserPropertyValue";

        public static final String USER_ID = "TestUserId";

        static {
            try {
                EVENT_PARAMS.put("param_1", "value_1");
                EVENT_PARAMS.put("param_2", "value_2");

                ITEMS_PARAMS.put("param_item_id", "SKU123");
                ITEMS_PARAMS.put("param_item_name", "Item 123");
                ITEMS_PARAMS.put("param_item_category", "Category 123");
                ITEMS_PARAMS.put("param_item_variant", "Variant 123");
                ITEMS_PARAMS.put("param_item_brand", "Brand 123");
                ITEMS_PARAMS.put("param_price", 28.00);

                ITEMS_ARRAY.put(ITEMS_PARAMS);
                ITEMS_ARRAY.put(ITEMS_PARAMS);

                ECOMMERCE_PARAMS.put("param_items", ITEMS_ARRAY);
                ECOMMERCE_PARAMS.put("param_value", 40.00);
                ECOMMERCE_PARAMS.put("param_tax", 10.00);
                ECOMMERCE_PARAMS.put("param_shipping", 8.00);
                ECOMMERCE_PARAMS.put("param_currency", "GBP");

                INVALID_ECOMMERCE_PARAMS.put("param_value", "String-Value");
                INVALID_ECOMMERCE_PARAMS.put("param_tax", "String-Tax");
                INVALID_ECOMMERCE_PARAMS.put("param_shipping", "String-Shipping");
                INVALID_ECOMMERCE_PARAMS.put("param_currency", null);
            } catch (JSONException ignored) {

            }
        }
    }

    public static final class Methods {
        private Methods() {
        }

        public static final String CONFIGURE = "configure";
        public static final String LOG_EVENT = "logEvent";
        public static final String SET_USER_PROPERTY = "setUserProperty";
        public static final String SET_USER_ID = "setUserId";
        public static final String SET_SCREEN_NAME = "setScreenName";
        public static final String RESET_DATA = "resetData";
    }

}
