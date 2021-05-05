package com.tealium.remotecommands.firebase;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class FirebaseRemoteCommandTests extends ActivityTestRule<QAActivity> {


    public FirebaseRemoteCommandTests() {
        super(QAActivity.class);
    }

    public MockFirebaseRemoteCommand newMockFirebaseRemoteCommand() {
        return new MockFirebaseRemoteCommand(QAActivity.getActivity().getApplication());
    }

    public MockFirebaseInstance newMockFirebaseWrapper() {
        return new MockFirebaseInstance(QAActivity.getActivity().getApplicationContext());
    }

    @Rule
    public ActivityTestRule<QAActivity> QAActivity = new ActivityTestRule<>(com.tealium.remotecommands.firebase.QAActivity.class);

    @Test
    public void testConfigureWithValidParams() {
        List<String> expectedMethods = new ArrayList<>();
        expectedMethods.add(TestData.Methods.CONFIGURE);

        MockFirebaseRemoteCommand mockRemoteCommand = newMockFirebaseRemoteCommand();
        MockFirebaseInstance mockInstance = new MockFirebaseInstance(QAActivity.getActivity().getApplication()) {
            @Override
            public void configure(Integer timeout, Integer minSeconds, Boolean analyticsEnabled) {
                super.configure(timeout, minSeconds, analyticsEnabled);
                Assert.assertEquals("Unexpected timeout value", (int) timeout, TestData.Values.SESSION_TIMEOUT * 1000);
                Assert.assertEquals("Unexpected minSeconds value", (int) minSeconds, TestData.Values.MIN_SECONDS * 1000);
                Assert.assertSame("Unexpected analyticsEnabled value", analyticsEnabled, TestData.Values.ANALYTICS_ENABLED_FALSE);
            }
        };
        mockRemoteCommand.setCommand(mockInstance);

        try {
            mockRemoteCommand.onInvoke(TestData.Responses.getValidConfig());
            TestUtils.assertContainsAllAndOnly(mockInstance.methodsCalled, expectedMethods);
        } catch (Exception e) {
            Assert.fail("No exceptions should be thrown.");
        }
    }

    @Test
    public void testConfigureWithInvalidParams() {
        List<String> expectedMethods = new ArrayList<>();
        expectedMethods.add(TestData.Methods.CONFIGURE);

        MockFirebaseRemoteCommand mockRemoteCommand = newMockFirebaseRemoteCommand();
        MockFirebaseInstance mockInstance = new MockFirebaseInstance(QAActivity.getActivity().getApplication()) {
            @Override
            public void configure(Integer timeout, Integer minSeconds, Boolean analyticsEnabled) {
                super.configure(timeout, minSeconds, analyticsEnabled);
                Assert.assertTrue("Unexpected timeout value", timeout <= 0);
                Assert.assertTrue("Unexpected minSeconds value", minSeconds <= 0);
                Assert.assertTrue("Unexpected analyticsEnabled value", analyticsEnabled);
            }
        };
        mockRemoteCommand.setCommand(mockInstance);

        try {
            mockRemoteCommand.onInvoke(TestData.Responses.getInvalidConfig());
            TestUtils.assertContainsAllAndOnly(mockInstance.methodsCalled, expectedMethods);
        } catch (Exception e) {
            Assert.fail("No exceptions should be thrown.");
        }
    }

    @Test
    public void testLogEventWithValidParams() {
        List<String> expectedMethods = new ArrayList<>();
        expectedMethods.add(TestData.Methods.LOG_EVENT);

        MockFirebaseRemoteCommand mockRemoteCommand = newMockFirebaseRemoteCommand();
        MockFirebaseInstance mockInstance = new MockFirebaseInstance(QAActivity.getActivity().getApplicationContext()) {
            @Override
            public void logEvent(String eventName, JSONObject eventParams) {
                super.logEvent(eventName, eventParams);

                Assert.assertEquals("Unexpected eventName value", eventName, TestData.Values.EVENT_NAME);
                Assert.assertEquals("Unexpected eventParams value", eventParams.toString(), TestData.Values.EVENT_PARAMS.toString());
            }
        };

        mockRemoteCommand.setCommand(mockInstance);

        try {
            mockRemoteCommand.onInvoke(TestData.Responses.getValidEvent());
            TestUtils.assertContainsAllAndOnly(mockInstance.methodsCalled, expectedMethods);
        } catch (Exception e) {
            Assert.fail("No exceptions should be thrown.");
        }
    }

    @Test
    public void testLogTagEventWithValidParams() {
        List<String> expectedMethods = new ArrayList<>();
        expectedMethods.add(TestData.Methods.LOG_EVENT);

        MockFirebaseRemoteCommand mockRemoteCommand = newMockFirebaseRemoteCommand();
        MockFirebaseInstance mockInstance = new MockFirebaseInstance(QAActivity.getActivity().getApplicationContext()) {
            @Override
            public void logEvent(String eventName, JSONObject eventParams) {
                super.logEvent(eventName, eventParams);

                Assert.assertEquals("Unexpected eventName value", eventName, TestData.Values.EVENT_NAME);
                Assert.assertEquals("Unexpected eventParams value", eventParams.toString(), TestData.Values.EVENT_PARAMS.toString());
            }
        };

        mockRemoteCommand.setCommand(mockInstance);

        try {
            mockRemoteCommand.onInvoke(TestData.Responses.getValidTagEvent());
            TestUtils.assertContainsAllAndOnly(mockInstance.methodsCalled, expectedMethods);
        } catch (Exception e) {
            Assert.fail("No exceptions should be thrown.");
        }
    }

    @Test
    public void testLogEventWithInvalidParams() {
        List<String> expectedMethods = new ArrayList<>();
        expectedMethods.add(TestData.Methods.LOG_EVENT);

        MockFirebaseRemoteCommand mockRemoteCommand = newMockFirebaseRemoteCommand();
        MockFirebaseInstance mockInstance = new MockFirebaseInstance(QAActivity.getActivity().getApplicationContext()) {
            @Override
            public void logEvent(String eventName, JSONObject eventParams) {
                super.logEvent(eventName, eventParams);

                Assert.assertNull("Unexpected eventName value", eventName);
                Assert.assertEquals("{}", eventParams.toString());
            }
        };

        mockRemoteCommand.setCommand(mockInstance);

        try {
            mockRemoteCommand.onInvoke(TestData.Responses.getInvalidEvent());
            TestUtils.assertContainsAllAndOnly(mockInstance.methodsCalled, expectedMethods);
        } catch (Exception e) {
            Assert.fail("No exceptions should be thrown.");
        }
    }

    @Test
    public void testLogTagEventWithInvalidParams() {
        List<String> expectedMethods = new ArrayList<>();
        expectedMethods.add(TestData.Methods.LOG_EVENT);

        MockFirebaseRemoteCommand mockRemoteCommand = newMockFirebaseRemoteCommand();
        MockFirebaseInstance mockInstance = new MockFirebaseInstance(QAActivity.getActivity().getApplicationContext()) {
            @Override
            public void logEvent(String eventName, JSONObject eventParams) {
                super.logEvent(eventName, eventParams);

                Assert.assertNull("Unexpected eventName value", eventName);
                Assert.assertEquals("{}", eventParams.toString());
            }
        };

        mockRemoteCommand.setCommand(mockInstance);

        try {
            mockRemoteCommand.onInvoke(TestData.Responses.getInvalidTagEvent());
            TestUtils.assertContainsAllAndOnly(mockInstance.methodsCalled, expectedMethods);
        } catch (Exception e) {
            Assert.fail("No exceptions should be thrown.");
        }
    }

    @Test
    public void testLogEventWithValidTagEcommerceParams() {
        List<String> expectedMethods = new ArrayList<>();
        expectedMethods.add(TestData.Methods.LOG_EVENT);

        MockFirebaseRemoteCommand mockRemoteCommand = newMockFirebaseRemoteCommand();
        MockFirebaseInstance mockInstance = new MockFirebaseInstance(QAActivity.getActivity().getApplicationContext()) {
            @Override
            public void logEvent(String eventName, JSONObject eventParams) {
                super.logEvent(eventName, eventParams);

                Assert.assertEquals("Unexpected eventName value", eventName, TestData.Values.EVENT_NAME);
                Assert.assertEquals("Unexpected eventParams value", eventParams.toString(), TestData.Values.TAG_ECOMMERCE_PARAMS.toString());

                try {
                    Bundle paramBundle = jsonToBundle(eventParams);
                    Assert.assertEquals("Value does not match.", 40.00D, paramBundle.getDouble(FirebaseAnalytics.Param.VALUE), 0D);
                    Assert.assertEquals("Tax does not match.", 10.00D, paramBundle.getDouble(FirebaseAnalytics.Param.TAX), 0D);
                    Assert.assertEquals("Shipping does not match.", 8.00D, paramBundle.getDouble(FirebaseAnalytics.Param.SHIPPING), 0D);
                    Assert.assertEquals("Currency does not match.", "GBP", paramBundle.getString(FirebaseAnalytics.Param.CURRENCY));

                    Parcelable[] itemsBundle = paramBundle.getParcelableArray("items");
                    Assert.assertEquals("Item count does not match.", 2, itemsBundle.length);
                    for (Parcelable item : itemsBundle) {
                        Bundle itemBundle = (Bundle) item;
                        Assert.assertEquals("Item Id does not match.", "SKU123", itemBundle.getString(FirebaseAnalytics.Param.ITEM_ID));
                        Assert.assertEquals("Item Name does not match.", "Item 123", itemBundle.getString(FirebaseAnalytics.Param.ITEM_NAME));
                    }
                } catch (JSONException jex) {
                    Assert.fail();
                }
            }
        };

        mockRemoteCommand.setCommand(mockInstance);

        try {
            mockRemoteCommand.onInvoke(TestData.Responses.getValidTagEcommerceEvent());
            TestUtils.assertContainsAllAndOnly(mockInstance.methodsCalled, expectedMethods);
        } catch (Exception e) {
            Assert.fail("No exceptions should be thrown.");
        }
    }

    @Test
    public void testLogEventWithValidJsonEcommerceParams() {
        List<String> expectedMethods = new ArrayList<>();
        expectedMethods.add(TestData.Methods.LOG_EVENT);

        MockFirebaseRemoteCommand mockRemoteCommand = newMockFirebaseRemoteCommand();
        MockFirebaseInstance mockInstance = new MockFirebaseInstance(QAActivity.getActivity().getApplicationContext()) {
            @Override
            public void logEvent(String eventName, JSONObject eventParams) {
                super.logEvent(eventName, eventParams);

                Assert.assertEquals("Unexpected eventName value", eventName, TestData.Values.EVENT_NAME);
                Assert.assertEquals("Unexpected eventParams value", eventParams.toString(), TestData.Values.JSON_ECOMMERCE_PARAMS.toString());

                try {
                    Bundle paramBundle = jsonToBundle(eventParams);
                    Assert.assertEquals("Value does not match.", 40.00D, paramBundle.getDouble(FirebaseAnalytics.Param.VALUE), 0D);
                    Assert.assertEquals("Tax does not match.", 10.00D, paramBundle.getDouble(FirebaseAnalytics.Param.TAX), 0D);
                    Assert.assertEquals("Shipping does not match.", 8.00D, paramBundle.getDouble(FirebaseAnalytics.Param.SHIPPING), 0D);
                    Assert.assertEquals("Currency does not match.", "GBP", paramBundle.getString(FirebaseAnalytics.Param.CURRENCY));

                    Parcelable[] itemsBundle = paramBundle.getParcelableArray("items");
                    Assert.assertEquals("Item count does not match.", 2, itemsBundle.length);
                    for (Parcelable item : itemsBundle) {
                        Bundle itemBundle = (Bundle) item;
                        Assert.assertEquals("Item Id does not match.", "SKU123", itemBundle.getString(FirebaseAnalytics.Param.ITEM_ID));
                        Assert.assertEquals("Item Name does not match.", "Item 123", itemBundle.getString(FirebaseAnalytics.Param.ITEM_NAME));
                    }
                } catch (JSONException jex) {
                    Assert.fail();
                }
            }
        };

        mockRemoteCommand.setCommand(mockInstance);

        try {
            mockRemoteCommand.onInvoke(TestData.Responses.getValidJsonEcommerceEvent());
            TestUtils.assertContainsAllAndOnly(mockInstance.methodsCalled, expectedMethods);
        } catch (Exception e) {
            Assert.fail("No exceptions should be thrown.");
        }
    }

    @Test
    public void testLogEventWithInvalidEcommerceParams() {
        List<String> expectedMethods = new ArrayList<>();
        expectedMethods.add(TestData.Methods.LOG_EVENT);

        MockFirebaseRemoteCommand mockRemoteCommand = newMockFirebaseRemoteCommand();
        MockFirebaseInstance mockInstance = new MockFirebaseInstance(QAActivity.getActivity().getApplicationContext()) {
            @Override
            public void logEvent(String eventName, JSONObject eventParams) {
                super.logEvent(eventName, eventParams);

                Assert.assertEquals("Unexpected eventName value", eventName, TestData.Values.EVENT_NAME);
                Assert.assertEquals("Unexpected eventParams value", eventParams.toString(), TestData.Values.INVALID_ECOMMERCE_PARAMS.toString());

                try {
                    Bundle paramBundle = jsonToBundle(eventParams);
                    Assert.assertEquals("Value does not match.", "String-Value", paramBundle.getString(FirebaseAnalytics.Param.VALUE));
                    Assert.assertEquals("Tax does not match.", "String-Tax", paramBundle.getString(FirebaseAnalytics.Param.TAX));
                    Assert.assertEquals("Shipping does not match.", "String-Shipping", paramBundle.getString(FirebaseAnalytics.Param.SHIPPING));
                    Assert.assertNull("Currency should be null.", paramBundle.getString(FirebaseAnalytics.Param.CURRENCY));
                } catch (JSONException jex) {
                    Assert.fail();
                }
            }
        };

        mockRemoteCommand.setCommand(mockInstance);

        try {
            mockRemoteCommand.onInvoke(TestData.Responses.getInvalidEcommerceEvent());
            TestUtils.assertContainsAllAndOnly(mockInstance.methodsCalled, expectedMethods);
        } catch (Exception e) {
            Assert.fail("No exceptions should be thrown.");
        }
    }

    @Test
    public void testScreenNameWithValidParams() {
        List<String> expectedMethods = new ArrayList<>();
        expectedMethods.add(TestData.Methods.SET_SCREEN_NAME);

        MockFirebaseRemoteCommand mockRemoteCommand = newMockFirebaseRemoteCommand();
        MockFirebaseInstance mockInstance = new MockFirebaseInstance(QAActivity.getActivity().getApplicationContext()) {
            @Override
            public void setScreenName(Activity activity, String screenName, String screenClass) {
                super.setScreenName(activity, screenName, screenClass);

                Assert.assertEquals("Unexpected screenName value", screenName, TestData.Values.SCREEN_NAME);
                Assert.assertEquals("Unexpected screenClass value", screenClass, TestData.Values.SCREEN_CLASS);
            }
        };

        mockRemoteCommand.setCommand(mockInstance);

        try {
            mockRemoteCommand.onInvoke(TestData.Responses.getValidScreenName());
            TestUtils.assertContainsAllAndOnly(mockInstance.methodsCalled, expectedMethods);
        } catch (Exception e) {
            Assert.fail("No exceptions should be thrown.");
        }
    }

    @Test
    public void testScreenNameWithInvalidParams() {
        List<String> expectedMethods = new ArrayList<>();
        expectedMethods.add(TestData.Methods.SET_SCREEN_NAME);

        MockFirebaseRemoteCommand mockRemoteCommand = newMockFirebaseRemoteCommand();
        MockFirebaseInstance mockInstance = new MockFirebaseInstance(QAActivity.getActivity().getApplicationContext()) {
            @Override
            public void setScreenName(Activity activity, String screenName, String screenClass) {
                super.setScreenName(activity, screenName, screenClass);

                Assert.assertNull("Unexpected screenName value", screenName);
                Assert.assertNull("Unexpected screenClass value", screenClass);
            }
        };

        mockRemoteCommand.setCommand(mockInstance);

        try {
            mockRemoteCommand.onInvoke(TestData.Responses.getInvalidScreenName());
            TestUtils.assertContainsAllAndOnly(mockInstance.methodsCalled, expectedMethods);
        } catch (Exception e) {
            Assert.fail("No exceptions should be thrown.");
        }
    }

    @Test
    public void testUserPropertyWithValidParams() {
        List<String> expectedMethods = new ArrayList<>();
        expectedMethods.add(TestData.Methods.SET_USER_PROPERTY);

        MockFirebaseRemoteCommand mockRemoteCommand = newMockFirebaseRemoteCommand();
        MockFirebaseInstance mockInstance = new MockFirebaseInstance(QAActivity.getActivity().getApplicationContext()) {
            @Override
            public void setUserProperty(String propertyName, String propertyValue) {
                super.setUserProperty(propertyName, propertyValue);

                Assert.assertEquals("Unexpected propertyName value", propertyName, TestData.Values.USER_PROPERTY_NAME);
                Assert.assertEquals("Unexpected propertyValue value", propertyValue, TestData.Values.USER_PROPERTY_VALUE);
            }
        };

        mockRemoteCommand.setCommand(mockInstance);

        try {
            mockRemoteCommand.onInvoke(TestData.Responses.getValidUserProperty());
            TestUtils.assertContainsAllAndOnly(mockInstance.methodsCalled, expectedMethods);
        } catch (Exception e) {
            Assert.fail("No exceptions should be thrown.");
        }
    }

    @Test
    public void testUserPropertyWithInvalidParams() {
        List<String> expectedMethods = new ArrayList<>();
        expectedMethods.add(TestData.Methods.SET_USER_PROPERTY);

        MockFirebaseRemoteCommand mockRemoteCommand = newMockFirebaseRemoteCommand();
        MockFirebaseInstance mockInstance = new MockFirebaseInstance(QAActivity.getActivity().getApplicationContext()) {
            @Override
            public void setUserProperty(String propertyName, String propertyValue) {
                super.setUserProperty(propertyName, propertyValue);

                Assert.assertNull("Unexpected propertyName value", propertyName);
                Assert.assertNull("Unexpected propertyValue value", propertyValue);
            }
        };

        mockRemoteCommand.setCommand(mockInstance);

        try {
            mockRemoteCommand.onInvoke(TestData.Responses.getInvalidUserProperty());
            TestUtils.assertContainsAllAndOnly(mockInstance.methodsCalled, expectedMethods);
        } catch (Exception e) {
            Assert.fail("No exceptions should be thrown.");
        }
    }

    @Test
    public void testUserIdWithValidParams() {
        List<String> expectedMethods = new ArrayList<>();
        expectedMethods.add(TestData.Methods.SET_USER_ID);

        MockFirebaseRemoteCommand mockRemoteCommand = newMockFirebaseRemoteCommand();
        MockFirebaseInstance mockInstance = new MockFirebaseInstance(QAActivity.getActivity().getApplicationContext()) {
            @Override
            public void setUserId(String userId) {
                super.setUserId(userId);

                Assert.assertEquals("Unexpected userId value", userId, TestData.Values.USER_ID);
            }
        };

        mockRemoteCommand.setCommand(mockInstance);

        try {
            mockRemoteCommand.onInvoke(TestData.Responses.getValidUserId());
            TestUtils.assertContainsAllAndOnly(mockInstance.methodsCalled, expectedMethods);
        } catch (Exception e) {
            Assert.fail("No exceptions should be thrown.");
        }
    }

    @Test
    public void testUserIdWithInvalidParams() {
        List<String> expectedMethods = new ArrayList<>();
        expectedMethods.add(TestData.Methods.SET_USER_ID);

        MockFirebaseRemoteCommand mockRemoteCommand = newMockFirebaseRemoteCommand();
        MockFirebaseInstance mockInstance = new MockFirebaseInstance(QAActivity.getActivity().getApplicationContext()) {
            @Override
            public void setUserId(String userId) {
                super.setUserId(userId);

                Assert.assertNull("Unexpected userId value", userId);
            }
        };

        mockRemoteCommand.setCommand(mockInstance);

        try {
            mockRemoteCommand.onInvoke(TestData.Responses.getInvalidUserId());
            TestUtils.assertContainsAllAndOnly(mockInstance.methodsCalled, expectedMethods);
        } catch (Exception e) {
            Assert.fail("No exceptions should be thrown.");
        }
    }

    @Test
    public void testResetData() {
        List<String> expectedMethods = new ArrayList<>();
        expectedMethods.add(TestData.Methods.RESET_DATA);

        MockFirebaseRemoteCommand mockRemoteCommand = newMockFirebaseRemoteCommand();
        MockFirebaseInstance mockInstance = newMockFirebaseWrapper();

        mockRemoteCommand.setCommand(mockInstance);

        try {
            mockRemoteCommand.onInvoke(TestData.Responses.getResetData());
            TestUtils.assertContainsAllAndOnly(mockInstance.methodsCalled, expectedMethods);
        } catch (Exception e) {
            Assert.fail("No exceptions should be thrown.");
        }
    }

    @Test
    public void testCompositeCommands() {
        List<String> expectedMethods = new ArrayList<>();
        expectedMethods.add(TestData.Methods.CONFIGURE);
        expectedMethods.add(TestData.Methods.LOG_EVENT);
        expectedMethods.add(TestData.Methods.SET_SCREEN_NAME);
        expectedMethods.add(TestData.Methods.SET_USER_PROPERTY);
        expectedMethods.add(TestData.Methods.SET_USER_ID);
        expectedMethods.add(TestData.Methods.RESET_DATA);

        MockFirebaseRemoteCommand mockRemoteCommand = newMockFirebaseRemoteCommand();
        MockFirebaseInstance mockInstance = newMockFirebaseWrapper();

        mockRemoteCommand.setCommand(mockInstance);

        try {
            mockRemoteCommand.onInvoke(TestData.Responses.getCompositeData());
            TestUtils.assertContainsAllAndOnly(mockInstance.methodsCalled, expectedMethods);
        } catch (Exception e) {
            Assert.fail("No exceptions should be thrown.");
        }
    }
}
