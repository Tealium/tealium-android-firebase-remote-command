package com.tealium.remotecommands.firebase;


import static com.tealium.remotecommands.firebase.FirebaseConstants.Commands;
import static com.tealium.remotecommands.firebase.FirebaseConstants.ItemProperties;
import static com.tealium.remotecommands.firebase.FirebaseConstants.Keys;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.AdditionalMatchers.leq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import android.app.Activity;
import android.app.Application;

import androidx.test.core.app.ApplicationProvider;

import com.tealium.remotecommands.RemoteCommand;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class FirebaseRemoteCommandTests {

    FirebaseRemoteCommand firebaseRemoteCommand;
    Application context = ApplicationProvider.getApplicationContext();
    Activity activity;
    FirebaseInstance mockFirebaseInstance;


    @Before
    public void setUp() {
        activity = mock();

        firebaseRemoteCommand = new FirebaseRemoteCommand(context);

        mockFirebaseInstance = mock(FirebaseInstance.class);
        firebaseRemoteCommand.mFirebaseCommand = mockFirebaseInstance;
        firebaseRemoteCommand.mCurrentActivity = activity;
    }

    @Test
    public void testConfigureWithValidParams() {
        RemoteCommand.Response response = ResponseBuilder.create()
                .addCommand(Commands.CONFIGURE)
                .populatePayload((json) -> {
                    json.put(Keys.SESSION_TIMEOUT, 36000);
                    json.put(Keys.ANALYTICS_ENABLED, false);
                    json.put(Keys.MIN_SECONDS, 3600); // ignored
                })
                .build();

        firebaseRemoteCommand.onInvoke(response);

        verify(mockFirebaseInstance).configure(36000 * 1000, false);
    }

    @Test
    public void testConfigureWithInvalidParams() {
        RemoteCommand.Response response = ResponseBuilder.create()
                .addCommand(Commands.CONFIGURE)
                .populatePayload((json) -> {
                    json.put(Keys.SESSION_TIMEOUT, null);
                    json.put(Keys.ANALYTICS_ENABLED, null);
                    json.put(Keys.MIN_SECONDS, null); // ignored
                })
                .build();

        firebaseRemoteCommand.onInvoke(response);

        verify(mockFirebaseInstance).configure(leq(0), eq(true));
    }

    @Test
    public void testLogEventWithValidParams() throws JSONException {
        JSONObject eventParams = new JSONObject();
        eventParams.put("param_1", "value_1");
        eventParams.put("param_2", "value_2");

        RemoteCommand.Response response = ResponseBuilder.create()
                .addCommand(Commands.LOG_EVENT)
                .populatePayload((json) -> {
                    json.put(Keys.EVENT_NAME, "TestEvent");
                    json.put(Keys.EVENT_PARAMS, eventParams);
                })
                .build();

        firebaseRemoteCommand.onInvoke(response);

        verify(mockFirebaseInstance).logEvent(eq("TestEvent"), eq(eventParams));
    }

    @Test
    public void testLogTagEventWithValidParams() throws JSONException {
        JSONObject eventParams = new JSONObject();

        RemoteCommand.Response response = ResponseBuilder.create()
                .addCommand(Commands.LOG_EVENT)
                .populatePayload((json) -> {
                    json.put(Keys.EVENT_NAME, "TestEvent");
                    json.put(Keys.TAG_EVENT_PARAMS, eventParams);
                })
                .build();

        firebaseRemoteCommand.onInvoke(response);

        verify(mockFirebaseInstance).logEvent(eq("TestEvent"), eq(eventParams));
    }

    @Test
    public void testLogEventWithInvalidParams() {
        RemoteCommand.Response response = ResponseBuilder.create()
                .addCommand(Commands.LOG_EVENT)
                .populatePayload((json) -> {
                    json.put(Keys.EVENT_NAME, null);
                    json.put(Keys.EVENT_PARAMS, null);
                })
                .build();

        firebaseRemoteCommand.onInvoke(response);

        verify(mockFirebaseInstance, never()).logEvent(any(), any());
    }

    @Test
    public void testLogTagEventWithInvalidParams() {
        RemoteCommand.Response response = ResponseBuilder.create()
                .addCommand(Commands.LOG_EVENT)
                .populatePayload((json) -> {
                    json.put(Keys.EVENT_NAME, null);
                    json.put(Keys.TAG_EVENT_PARAMS, null);
                })
                .build();

        firebaseRemoteCommand.onInvoke(response);

        verify(mockFirebaseInstance, never()).logEvent(any(), any());
    }

    @Test
    public void testLogEventWithValidTagEcommerceParams() throws JSONException {
        JSONObject eventParams = new JSONObject();
        JSONObject itemParams = new JSONObject();
        itemParams.put(ItemProperties.ID, "SKU-1");
        itemParams.put(ItemProperties.NAME, "Item 1");
        itemParams.put(ItemProperties.PRICE, 10.99);

        RemoteCommand.Response response = ResponseBuilder.create()
                .addCommand(Commands.LOG_EVENT)
                .populatePayload((json) -> {
                    json.put(Keys.EVENT_NAME, "TestEvent");
                    json.put(Keys.TAG_EVENT_PARAMS, eventParams);
                    json.put(Keys.ITEMS_PARAMS, itemParams);
                })
                .build();

        firebaseRemoteCommand.onInvoke(response);

        ArgumentCaptor<JSONObject> paramCaptor = ArgumentCaptor.forClass(JSONObject.class);
        verify(mockFirebaseInstance).logEvent(eq("TestEvent"), paramCaptor.capture());

        JSONObject capturedParams = paramCaptor.getValue();
        assertEquals(eventParams, capturedParams);

        JSONArray items = capturedParams.optJSONArray("param_items");
        assertNotNull(items);

        JSONObject firstItem = items.getJSONObject(0);
        assertEquals("SKU-1", firstItem.getString(ItemProperties.ID));
        assertEquals("Item 1", firstItem.getString(ItemProperties.NAME));
        assertEquals(10.99, firstItem.getDouble(ItemProperties.PRICE), 0.0);
    }

    @Test
    public void testLogEventWithValidJsonEcommerceParamsSingle() throws JSONException {
        JSONObject eventParams = new JSONObject();
        JSONObject itemParams = new JSONObject();
        itemParams.put(ItemProperties.ID, "SKU-1");
        itemParams.put(ItemProperties.NAME, "Item 1");
        itemParams.put(ItemProperties.PRICE, 10.99);

        RemoteCommand.Response response = ResponseBuilder.create()
                .addCommand(Commands.LOG_EVENT)
                .populatePayload((json) -> {
                    json.put(Keys.EVENT_NAME, "TestEvent");
                    json.put(Keys.EVENT_PARAMS, eventParams);
                    json.put(Keys.ITEMS_PARAMS, itemParams);
                })
                .build();

        firebaseRemoteCommand.onInvoke(response);

        ArgumentCaptor<JSONObject> paramCaptor = ArgumentCaptor.forClass(JSONObject.class);
        verify(mockFirebaseInstance).logEvent(eq("TestEvent"), paramCaptor.capture());

        JSONObject capturedParams = paramCaptor.getValue();
        assertEquals(eventParams, capturedParams);

        JSONArray items = capturedParams.optJSONArray("param_items");
        assertNotNull(items);

        JSONObject firstItem = items.getJSONObject(0);
        assertEquals("SKU-1", firstItem.getString(ItemProperties.ID));
        assertEquals("Item 1", firstItem.getString(ItemProperties.NAME));
        assertEquals(10.99, firstItem.getDouble(ItemProperties.PRICE), 0.0);
    }

    @Test
    public void testLogEventWithItemsArray() throws JSONException {
        JSONObject eventParams = new JSONObject();
        JSONObject itemParams = new JSONObject();

        for (int i = 1; i <= 3; i ++) {
             itemParams.accumulate(ItemProperties.ID, "SKU-" + i);
             itemParams.accumulate(ItemProperties.NAME, "Item " + i);
             itemParams.accumulate(ItemProperties.PRICE, i);
        }

        RemoteCommand.Response response = ResponseBuilder.create()
                .addCommand(Commands.LOG_EVENT)
                .populatePayload((json) -> {
                    json.put(Keys.EVENT_NAME, "TestEvent");
                    json.put(Keys.EVENT_PARAMS, eventParams);
                    json.put(Keys.ITEMS_PARAMS, itemParams);
                })
                .build();

        firebaseRemoteCommand.onInvoke(response);

        ArgumentCaptor<JSONObject> paramCaptor = ArgumentCaptor.forClass(JSONObject.class);
        verify(mockFirebaseInstance).logEvent(eq("TestEvent"), paramCaptor.capture());

        JSONObject capturedParams = paramCaptor.getValue();
        assertEquals(eventParams, capturedParams);

        JSONArray items = capturedParams.optJSONArray("param_items");
        assertNotNull(items);

        for (int i = 1; i <= items.length(); i++) {
            JSONObject item = items.getJSONObject(i - 1);
            assertEquals("SKU-" + i, item.getString(ItemProperties.ID));
            assertEquals("Item " + i, item.getString(ItemProperties.NAME));
            assertEquals(i, item.getInt(ItemProperties.PRICE));
        }
    }

    @Test
    public void testUserPropertyWithValidStringParams() {
        RemoteCommand.Response response = ResponseBuilder.create()
                .addCommand(Commands.SET_USER_PROPERTY)
                .populatePayload((json) -> {
                    json.put(Keys.USER_PROPERTY_NAME, "TestUserPropertyName");
                    json.put(Keys.USER_PROPERTY_VALUE, "TestUserPropertyValue");
                })
                .build();

        firebaseRemoteCommand.onInvoke(response);

        verify(mockFirebaseInstance).setUserProperty("TestUserPropertyName", "TestUserPropertyValue");
    }

    @Test
    public void testUserPropertyWithValidArrayParams() {
        JSONArray propertyNames = new JSONArray();
        propertyNames.put("prop1");
        propertyNames.put("prop2");

        JSONArray propertyValues = new JSONArray();
        propertyValues.put("value1");
        propertyValues.put("value2");

        RemoteCommand.Response response = ResponseBuilder.create()
                .addCommand(Commands.SET_USER_PROPERTY)
                .populatePayload((json) -> {
                    json.put(Keys.USER_PROPERTY_NAME, propertyNames);
                    json.put(Keys.USER_PROPERTY_VALUE, propertyValues);
                })
                .build();

        firebaseRemoteCommand.onInvoke(response);

        verify(mockFirebaseInstance).setUserProperty("prop1", "value1");
        verify(mockFirebaseInstance).setUserProperty("prop2", "value2");
    }

    @Test
    public void testUserPropertyWithInvalidParams() {
        RemoteCommand.Response response = ResponseBuilder.create()
                .addCommand(Commands.SET_USER_PROPERTY)
                .populatePayload((json) -> {
                    json.put(Keys.USER_PROPERTY_NAME, null);
                    json.put(Keys.USER_PROPERTY_VALUE, null);
                })
                .build();

        firebaseRemoteCommand.onInvoke(response);

        verify(mockFirebaseInstance, never()).setUserProperty(any(), any());
    }

    @Test
    public void testUserPropertyWithInvalidArrayParams() {
        JSONArray propertyNames = new JSONArray();
        propertyNames.put("prop1");
        propertyNames.put("prop2");

        JSONArray propertyValues = new JSONArray();
        propertyValues.put("value1");

        RemoteCommand.Response response = ResponseBuilder.create()
                .addCommand(Commands.SET_USER_PROPERTY)
                .populatePayload((json) -> {
                    json.put(Keys.USER_PROPERTY_NAME, propertyNames);
                    json.put(Keys.USER_PROPERTY_VALUE, propertyValues);
                })
                .build();

        firebaseRemoteCommand.onInvoke(response);

        verify(mockFirebaseInstance).setUserProperty("prop1", "value1");
        verify(mockFirebaseInstance).setUserProperty("prop2", "");
    }

    @Test
    public void testSetConsentWithValidParams() throws JSONException {
        JSONObject consentJson = new JSONObject();
        consentJson.put("ad_storage", "granted");
        consentJson.put("ad_user_data", "denied");
        consentJson.put("ad_personalization", "denied");
        consentJson.put("analytics_storage", "granted");

        RemoteCommand.Response response = ResponseBuilder.create()
                .addCommand(Commands.SET_CONSENT)
                .populatePayload((json) -> {
                    json.put(Keys.CONSENT_SETTINGS, consentJson);
                })
                .build();

        firebaseRemoteCommand.onInvoke(response);

        verify(mockFirebaseInstance).setConsent(consentJson);
    }

    @Test
    public void testSetConsentWithMissingParams() {
        RemoteCommand.Response response = ResponseBuilder.create()
                .addCommand(Commands.SET_CONSENT)
                .build();

        firebaseRemoteCommand.onInvoke(response);

        verify(mockFirebaseInstance, never()).setConsent(any());
    }

    @Test
    public void testUserIdWithValidParams() {
        RemoteCommand.Response response = ResponseBuilder.create()
                .addCommand(Commands.SET_USER_ID)
                .populatePayload((json) -> {
                    json.put(Keys.USER_ID, "TestUserId");
                })
                .build();

        firebaseRemoteCommand.onInvoke(response);

        verify(mockFirebaseInstance).setUserId("TestUserId");
    }

    @Test
    public void testUserIdWithInvalidParams() {
        RemoteCommand.Response response = ResponseBuilder.create()
                .addCommand(Commands.SET_USER_ID)
                .build();

        firebaseRemoteCommand.onInvoke(response);

        verify(mockFirebaseInstance, never()).setUserId(any());
    }

    @Test
    public void testResetData() {
        RemoteCommand.Response response = ResponseBuilder.create()
                .addCommand(Commands.RESET_DATA)
                .build();

        firebaseRemoteCommand.onInvoke(response);

        verify(mockFirebaseInstance).resetData();
    }

    @Test
    public void testSetDefaultParameters() throws JSONException {
        JSONObject defaultParams = new JSONObject();
        defaultParams.put("key", "value");

        RemoteCommand.Response response = ResponseBuilder.create()
                .addCommand(Commands.SET_DEFAULT_PARAMETERS)
                .populatePayload((json) -> {
                    json.put(Keys.DEFAULT_PARAMS, defaultParams);
                })
                .build();

        firebaseRemoteCommand.onInvoke(response);

        verify(mockFirebaseInstance).setDefaultEventParameters(defaultParams);
    }

    @Test
    public void testSetDefaultParametersMissing() {
        RemoteCommand.Response response = ResponseBuilder.create()
                .addCommand(Commands.SET_DEFAULT_PARAMETERS)
                .build();

        firebaseRemoteCommand.onInvoke(response);

        verify(mockFirebaseInstance, never()).setDefaultEventParameters(any());
    }

    @Test
    public void testCompositeCommands() {
        RemoteCommand.Response response = ResponseBuilder.create()
                .addCommand(Commands.CONFIGURE)
                .addCommand(Commands.RESET_DATA)
                .addCommand(Commands.LOG_EVENT)
                .populatePayload(json -> {
                    json.put(Keys.EVENT_NAME, "TestEvent");
                })
                .build();

        firebaseRemoteCommand.onInvoke(response);

        verify(mockFirebaseInstance).configure(any(), any());
        verify(mockFirebaseInstance).resetData();
        verify(mockFirebaseInstance).logEvent(eq("TestEvent"), any());
    }

    private static class ResponseBuilder {

        private final List<String> commands;
        private final JSONObject payload;
        private String commandId;
        private String responseId;

        private ResponseBuilder() {
            commands = new ArrayList<>();
            payload = new JSONObject();
            commandId = null;
            responseId = null;
        }

        static ResponseBuilder create() {
            return new ResponseBuilder();
        }

        public ResponseBuilder addCommand(String commandName) {
            commands.add(commandName);
            return this;
        }

        public ResponseBuilder setCommandId(String commandId) {
            this.commandId = commandId;
            return this;
        }

        public ResponseBuilder setResponseId(String responseId) {
            this.responseId = responseId;
            return this;
        }

        public ResponseBuilder populatePayload(JsonBuilder jsonBuilder) {
            try {
                jsonBuilder.populateJson(payload);
            } catch (JSONException jex) {
                fail(jex.getMessage());
                jex.printStackTrace();
            }
            return this;
        }

        public RemoteCommand.Response build() {
            try {
                this.payload.put(Keys.COMMAND_NAME, String.join(FirebaseConstants.SEPARATOR, commands));
            } catch (JSONException jex) {
                fail();
            }
            return new RemoteCommand.Response(
                    null,
                    this.commandId != null ? this.commandId : "",
                    this.responseId != null ? this.responseId : "",
                    this.payload
            );
        }
    }

    @FunctionalInterface
    private interface JsonBuilder {
        void populateJson(JSONObject json) throws JSONException;
    }
}
