package com.tealium.remotecommands.firebase;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.test.core.app.ApplicationProvider;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.robolectric.RobolectricTestRunner;

import java.util.Map;

@RunWith(RobolectricTestRunner.class)
public class FirebaseInstanceTests {

    Application context = ApplicationProvider.getApplicationContext();
    Activity activity;

    MockedStatic<FirebaseAnalytics> mockFirebaseStatic;
    FirebaseAnalytics mockFirebaseAnalytics;

    FirebaseInstance firebaseInstance;

    @Before
    public void setUp() {
        activity = mock();

        mockFirebaseAnalytics = mock(FirebaseAnalytics.class);
        mockFirebaseStatic = mockStatic(FirebaseAnalytics.class);
        mockFirebaseStatic.when(() -> FirebaseAnalytics.getInstance(context))
                .thenReturn(mockFirebaseAnalytics);

        firebaseInstance = new FirebaseInstance(context);
    }

    @After
    public void tearDown() {
        mockFirebaseStatic.close();
    }

    @Test
    public void configure_SetsSessionTimeout() {
        firebaseInstance.configure(10, null);

        verify(mockFirebaseAnalytics).setSessionTimeoutDuration(10);
    }

    @Test
    public void configure_DoesNotSet_SessionTimeout_When_LessThanOne() {
        firebaseInstance.configure(0, null);
        firebaseInstance.configure(-1, null);

        verify(mockFirebaseAnalytics, never()).setSessionTimeoutDuration(anyLong());
    }

    @Test
    public void configure_SetsAnalyticsEnabled() {
        firebaseInstance.configure(null, true);

        verify(mockFirebaseAnalytics).setAnalyticsCollectionEnabled(true);
    }

    @Test
    public void configure_SetsBoth_AnalyticsEnabled_And_SessionTimeout() {
        firebaseInstance.configure(10, true);

        verify(mockFirebaseAnalytics).setAnalyticsCollectionEnabled(true);
    }

    @Test
    public void logEvent_DoesNothing_When_EventName_IsNull() {
        firebaseInstance.logEvent(null, new JSONObject());

        verify(mockFirebaseAnalytics, never()).logEvent(any(), any());
    }

    @Test
    public void logEvent_LogsAnEvent_When_EventName_IsNotNull() {
        firebaseInstance.logEvent("TestEvent", null);

        verify(mockFirebaseAnalytics).logEvent(eq("TestEvent"), any());
    }

    @Test
    public void logEvent_MapsKnownEvents_And_Params_To_FirebaseEvents() throws JSONException {
        JSONObject params = new JSONObject();
        params.put("param_achievement_id", "Some Achievement");

        firebaseInstance.logEvent("event_unlock_achievement", params);

        ArgumentCaptor<Bundle> bundleCaptor = ArgumentCaptor.forClass(Bundle.class);
        verify(mockFirebaseAnalytics).logEvent(eq(FirebaseAnalytics.Event.UNLOCK_ACHIEVEMENT), bundleCaptor.capture());

        Bundle bundle = bundleCaptor.getValue();
        assertEquals("Some Achievement", bundle.getString(FirebaseAnalytics.Param.ACHIEVEMENT_ID));
    }

    @Test
    public void setCurrentScreen_DoesNothing_When_ScreenName_IsNull() {
        firebaseInstance.setScreenName(activity, null, "ScreenClass");

        verify(mockFirebaseAnalytics, never()).setCurrentScreen(any(), any(), any());
    }

    @Test
    public void setCurrentScreen_SetsCurrentScreen_When_ScreenName_IsSet() {
        firebaseInstance.setScreenName(activity, "TestScreen", "ScreenClass");

        verify(mockFirebaseAnalytics).setCurrentScreen(activity, "TestScreen", "ScreenClass");
    }

    @Test
    public void setUserId_DoesNot_SetUserId_When_UserId_IsNull() {
        firebaseInstance.setUserId(null);

        verify(mockFirebaseAnalytics, never()).setUserId(any());
    }

    @Test
    public void setUserId_Sets_UserId_When_UserId_IsNotNull() {
        firebaseInstance.setUserId("User");

        verify(mockFirebaseAnalytics).setUserId("User");
    }

    @Test
    public void setUserProperty_DoesNot_SetUserProperty_When_PropertyName_IsNull() {
        firebaseInstance.setUserProperty(null, "Value");

        verify(mockFirebaseAnalytics, never()).setUserProperty(any(), any());
    }

    @Test
    public void setUserProperty_Sets_UserProperty_When_PropertyName_IsNotNull() {
        firebaseInstance.setUserProperty("Property", "Value");

        verify(mockFirebaseAnalytics).setUserProperty("Property", "Value");
    }

    @Test
    public void setUserProperty_Sets_Null_UserProperty_When_PropertyValue_IsBlank() {
        firebaseInstance.setUserProperty("Property", "");

        verify(mockFirebaseAnalytics).setUserProperty("Property", null);
    }

    @Test
    public void setDefaultEventParameters_DoesNot_SetDefaultEventParameters_WhenNull_Or_Empty() {
        firebaseInstance.setDefaultEventParameters(null);
        firebaseInstance.setDefaultEventParameters(new JSONObject());

        verify(mockFirebaseAnalytics, never()).setDefaultEventParameters(any());
    }

    @Test
    public void setDefaultEventParameters_Sets_DefaultEventParameters_WhenNotNull_And_NotEmpty() throws JSONException {
        JSONObject params = new JSONObject();
        params.put("default", "value");

        firebaseInstance.setDefaultEventParameters(params);

        ArgumentCaptor<Bundle> bundleCaptor = ArgumentCaptor.forClass(Bundle.class);
        verify(mockFirebaseAnalytics).setDefaultEventParameters(bundleCaptor.capture());

        Bundle bundle = bundleCaptor.getValue();
        assertEquals("value", bundle.getString("default"));
    }

    @Test
    public void setConsent_DoesNot_SetConsent_When_Parameters_AreNull_Or_Empty() {
        JSONObject params = new JSONObject();
        firebaseInstance.setConsent(null);
        firebaseInstance.setConsent(params);

        verify(mockFirebaseAnalytics, never()).setConsent(any());
    }

    @Test
    public void setConsent_Maps_And_SetsConsent_When_Parameters_AreValid() throws JSONException {
        JSONObject params = new JSONObject();
        params.put("ad_personalization", "granted");
        params.put("ad_storage", "granted");
        params.put("ad_user_data", "denied");
        params.put("analytics_storage", "denied");

        firebaseInstance.setConsent(params);

        @SuppressWarnings("unchecked")
        ArgumentCaptor<Map<FirebaseAnalytics.ConsentType, FirebaseAnalytics.ConsentStatus>> consentCaptor
                = ArgumentCaptor.forClass(Map.class);

        verify(mockFirebaseAnalytics).setConsent(consentCaptor.capture());

        Map<FirebaseAnalytics.ConsentType, FirebaseAnalytics.ConsentStatus> consent = consentCaptor.getValue();
        assertEquals(FirebaseAnalytics.ConsentStatus.GRANTED, consent.get(FirebaseAnalytics.ConsentType.AD_PERSONALIZATION));
        assertEquals(FirebaseAnalytics.ConsentStatus.GRANTED, consent.get(FirebaseAnalytics.ConsentType.AD_STORAGE));
        assertEquals(FirebaseAnalytics.ConsentStatus.DENIED, consent.get(FirebaseAnalytics.ConsentType.AD_USER_DATA));
        assertEquals(FirebaseAnalytics.ConsentStatus.DENIED, consent.get(FirebaseAnalytics.ConsentType.ANALYTICS_STORAGE));
    }

    @Test
    public void mapEvent_MapsTealiumEvents_ToFirebaseEvents() {
        for (Map.Entry<String, String> entry : FirebaseInstance.eventsMap.entrySet()) {
            assertEquals(entry.getValue(), FirebaseInstance.mapEventNames(entry.getKey()));
        }
    }

    @Test
    public void mapEvent_MapsUnknownFirebaseEvents() {
        // Tealium mapping is event_signup (no space)
        assertEquals("sign_up", FirebaseInstance.mapEventNames("event_sign_up"));
    }

    @Test
    public void mapParams_MapsTealiumParams_ToFirebaseParams() {
        for (Map.Entry<String, String> entry : FirebaseInstance.params.entrySet()) {
            assertEquals(entry.getValue(), FirebaseInstance.mapParams(entry.getKey()));
        }
    }

    @Test
    public void mapParams_MapsUnknownFirebaseParams() {
        // base dependency is 18.+ but campaign Id is only present in a much later release
        // successful test will therefore require a later dependency for this to pass.
        assertEquals("campaign_id", FirebaseInstance.mapParams("param_campaign_id"));
        assertEquals("creative_format", FirebaseInstance.mapParams("param_creative_format"));
        assertEquals("marketing_tactic", FirebaseInstance.mapParams("param_marketing_tactic"));
        assertEquals("source_platform", FirebaseInstance.mapParams("param_source_platform"));
    }

    @Test
    public void mapEventNames_ReturnsValue_When_NotMatched() {
        assertEquals("my_custom_event", FirebaseInstance.mapEventNames("my_custom_event"));
    }

    @Test
    public void mapParams_ReturnsValue_When_NotMatched() {
        assertEquals("my_custom_param", FirebaseInstance.mapParams("my_custom_param"));
    }
}
