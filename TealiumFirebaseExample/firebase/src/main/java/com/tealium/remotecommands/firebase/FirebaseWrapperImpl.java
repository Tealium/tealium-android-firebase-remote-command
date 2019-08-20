package com.tealium.remotecommands.firebase;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.tealium.remotecommands.firebase.FirebaseConstants.TAG;

class FirebaseWrapperImpl implements FirebaseWrapper {

    private FirebaseAnalytics mFirebaseAnalytics;

    private static Map<String, String> eventsMap;
    private static Map<String, String> params;

    static {
        eventsMap = new HashMap<>();
        eventsMap.put("event_add_payment_info", FirebaseAnalytics.Event.ADD_PAYMENT_INFO);
        eventsMap.put("event_add_to_cart", FirebaseAnalytics.Event.ADD_TO_CART);
        eventsMap.put("event_add_to_wishlist", FirebaseAnalytics.Event.ADD_TO_WISHLIST);
        eventsMap.put("event_app_open", FirebaseAnalytics.Event.APP_OPEN);
        eventsMap.put("event_begin_checkout", FirebaseAnalytics.Event.BEGIN_CHECKOUT);
        eventsMap.put("event_campaign_details", FirebaseAnalytics.Event.CAMPAIGN_DETAILS);
        eventsMap.put("event_checkout_progress", FirebaseAnalytics.Event.CHECKOUT_PROGRESS);
        eventsMap.put("event_earn_virtual_currency", FirebaseAnalytics.Event.EARN_VIRTUAL_CURRENCY);
        eventsMap.put("event_ecommerce_purchase", FirebaseAnalytics.Event.ECOMMERCE_PURCHASE);
        eventsMap.put("event_generate_lead", FirebaseAnalytics.Event.GENERATE_LEAD);
        eventsMap.put("event_join_group", FirebaseAnalytics.Event.JOIN_GROUP);
        eventsMap.put("event_level_up", FirebaseAnalytics.Event.LEVEL_UP);
        eventsMap.put("event_login", FirebaseAnalytics.Event.LOGIN);
        eventsMap.put("event_post_score", FirebaseAnalytics.Event.POST_SCORE);
        eventsMap.put("event_present_offer", FirebaseAnalytics.Event.PRESENT_OFFER);
        eventsMap.put("event_purchase_refund", FirebaseAnalytics.Event.PURCHASE_REFUND);
        eventsMap.put("event_remove_cart", FirebaseAnalytics.Event.REMOVE_FROM_CART);
        eventsMap.put("event_search", FirebaseAnalytics.Event.SEARCH);
        eventsMap.put("event_select_content", FirebaseAnalytics.Event.SELECT_CONTENT);
        eventsMap.put("event_set_checkout_option", FirebaseAnalytics.Event.SET_CHECKOUT_OPTION);
        eventsMap.put("event_share", FirebaseAnalytics.Event.SHARE);
        eventsMap.put("event_signup", FirebaseAnalytics.Event.SIGN_UP);
        eventsMap.put("event_spend_virtual_currency", FirebaseAnalytics.Event.SPEND_VIRTUAL_CURRENCY);
        eventsMap.put("event_tutorial_begin", FirebaseAnalytics.Event.TUTORIAL_BEGIN);
        eventsMap.put("event_tutorial_complete", FirebaseAnalytics.Event.TUTORIAL_COMPLETE);
        eventsMap.put("event_unlock_achievement", FirebaseAnalytics.Event.UNLOCK_ACHIEVEMENT);
        eventsMap.put("event_view_item", FirebaseAnalytics.Event.VIEW_ITEM);
        eventsMap.put("event_view_item_list", FirebaseAnalytics.Event.VIEW_ITEM);
        eventsMap.put("event_view_search_results", FirebaseAnalytics.Event.VIEW_SEARCH_RESULTS);

        params = new HashMap<>();
        params.put("param_achievement_id", FirebaseAnalytics.Param.ACHIEVEMENT_ID);
        params.put("param_ad_network_click_id", FirebaseAnalytics.Param.ACLID);
        params.put("param_affiliation", FirebaseAnalytics.Param.AFFILIATION);
        params.put("param_cp1", FirebaseAnalytics.Param.CP1);
        params.put("param_campaign", FirebaseAnalytics.Param.CAMPAIGN);
        params.put("param_character", FirebaseAnalytics.Param.CHARACTER);
        params.put("param_checkout_option", FirebaseAnalytics.Param.CHECKOUT_OPTION);
        params.put("param_checkout_step", FirebaseAnalytics.Param.CHECKOUT_STEP);
        params.put("param_content", FirebaseAnalytics.Param.CONTENT);
        params.put("param_content_type", FirebaseAnalytics.Param.CONTENT_TYPE);
        params.put("param_coupon", FirebaseAnalytics.Param.COUPON);
        params.put("param_creative_name", FirebaseAnalytics.Param.CREATIVE_NAME);
        params.put("param_creative_slot", FirebaseAnalytics.Param.CREATIVE_SLOT);
        params.put("param_currency", FirebaseAnalytics.Param.CURRENCY);
        params.put("param_destination", FirebaseAnalytics.Param.DESTINATION);
        params.put("param_end_date", FirebaseAnalytics.Param.END_DATE);
        params.put("param_flight_number", FirebaseAnalytics.Param.FLIGHT_NUMBER);
        params.put("param_group_id", FirebaseAnalytics.Param.GROUP_ID);
        params.put("param_index", FirebaseAnalytics.Param.INDEX);
        params.put("param_item_brand", FirebaseAnalytics.Param.ITEM_BRAND);
        params.put("param_item_category", FirebaseAnalytics.Param.ITEM_CATEGORY);
        params.put("param_item_id", FirebaseAnalytics.Param.ITEM_ID);
        params.put("param_item_list", FirebaseAnalytics.Param.ITEM_LIST);
        params.put("param_item_location_id", FirebaseAnalytics.Param.ITEM_LOCATION_ID);
        params.put("param_item_name", FirebaseAnalytics.Param.ITEM_NAME);
        params.put("param_item_variant", FirebaseAnalytics.Param.ITEM_VARIANT);
        params.put("param_level", FirebaseAnalytics.Param.LEVEL);
        params.put("param_location", FirebaseAnalytics.Param.LOCATION);
        params.put("param_medium", FirebaseAnalytics.Param.MEDIUM);
        params.put("param_number_nights", FirebaseAnalytics.Param.NUMBER_OF_NIGHTS);
        params.put("param_number_pax", FirebaseAnalytics.Param.NUMBER_OF_PASSENGERS);
        params.put("param_number_rooms", FirebaseAnalytics.Param.NUMBER_OF_ROOMS);
        params.put("param_origin", FirebaseAnalytics.Param.ORIGIN);
        params.put("param_price", FirebaseAnalytics.Param.PRICE);
        params.put("param_quantity", FirebaseAnalytics.Param.QUANTITY);
        params.put("param_score", FirebaseAnalytics.Param.SCORE);
        params.put("param_search_term", FirebaseAnalytics.Param.SEARCH_TERM);
        params.put("param_shipping", FirebaseAnalytics.Param.SHIPPING);
        params.put("param_source", FirebaseAnalytics.Param.SOURCE);
        params.put("param_start_date", FirebaseAnalytics.Param.START_DATE);
        params.put("param_tax", FirebaseAnalytics.Param.TAX);
        params.put("param_term", FirebaseAnalytics.Param.TERM);
        params.put("param_transaction_id", FirebaseAnalytics.Param.TRANSACTION_ID);
        params.put("param_travel_class", FirebaseAnalytics.Param.TRAVEL_CLASS);
        params.put("param_value", FirebaseAnalytics.Param.VALUE);
        params.put("param_virtual_currency_name", FirebaseAnalytics.Param.VIRTUAL_CURRENCY_NAME);
    }

    public FirebaseWrapperImpl(Context applicationContext) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(applicationContext);
    }

    @Override
    public void configure(Integer timeout, Integer minSeconds, Boolean analyticsEnabled) {
        if (timeout != null) {
            mFirebaseAnalytics.setSessionTimeoutDuration(timeout.longValue());
        }

        if (minSeconds != null) {
            mFirebaseAnalytics.setMinimumSessionDuration(minSeconds.longValue());
        }

        if (analyticsEnabled != null) {
            mFirebaseAnalytics.setAnalyticsCollectionEnabled(analyticsEnabled);
        }
    }

    @Override
    public void logEvent(String eventName, JSONObject eventParams) {
        Bundle paramBundle;
        try {
            paramBundle = jsonToBundle(eventParams);
        } catch (JSONException e) {
            paramBundle = null;
        } catch (NullPointerException e) {
            paramBundle = null;
        }
        if (eventName != null) {
            String ev = mapEventNames(eventName);
            mFirebaseAnalytics.logEvent(ev, paramBundle);
        }
    }

    @Override
    public void setScreenName(Activity currentActivity, String screenName, String screenClass) {
        if (screenName != null) {
            mFirebaseAnalytics.setCurrentScreen(currentActivity, screenName, screenClass);
        }
    }

    @Override
    public void setUserId(String userId) {
        if (userId != null) {
            mFirebaseAnalytics.setUserId(userId);
        }
    }

    @Override
    public void setUserProperty(String propertyName, String propertyValue) {
        if (propertyName != null && propertyValue != null) {
            mFirebaseAnalytics.setUserProperty(propertyName, propertyValue);
        }
    }

    @Override
    public void resetData() {
        mFirebaseAnalytics.resetAnalyticsData();
    }


    static Bundle jsonToBundle(JSONObject jsonObject) throws JSONException {
        Bundle bundle = new Bundle();
        Iterator iter = jsonObject.keys();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            String firebaseKey = mapParams(key);
            try {
                switch (firebaseKey) {
                    // Double
                    case FirebaseAnalytics.Param.VALUE:
                    case FirebaseAnalytics.Param.PRICE:
                    case FirebaseAnalytics.Param.TAX:
                    case FirebaseAnalytics.Param.SHIPPING:
                        bundle.putDouble(firebaseKey, jsonObject.getDouble(key));
                        break;
                    // Long
                    case FirebaseAnalytics.Param.QUANTITY:
                    case FirebaseAnalytics.Param.NUMBER_OF_NIGHTS:
                    case FirebaseAnalytics.Param.NUMBER_OF_ROOMS:
                    case FirebaseAnalytics.Param.NUMBER_OF_PASSENGERS:
                    case FirebaseAnalytics.Param.LEVEL:
                    case FirebaseAnalytics.Param.SCORE:
                        bundle.putLong(firebaseKey, jsonObject.getLong(key));
                        break;
                    // All others are Strings.
                    default:
                        bundle.putString(firebaseKey, jsonObject.getString(key));
                }
            } catch (JSONException ex) {
                Log.d(TAG, "jsonToBundle: Error converting value for key: " + firebaseKey + ". Adding as String.");
                if (!bundle.containsKey(firebaseKey)) {
                    bundle.putString(firebaseKey, jsonObject.getString(key));
                }
            }
        }
        return bundle;
    }

    private static String mapEventNames(String eventName) {

        String name = eventsMap.get(eventName);
        if (name == null) {
            return eventName;
        }
        return name;
    }

    private static String mapParams(String param) {
        String paramName = params.get(param);
        if (paramName == null) {
            return param;
        }
        return paramName;
    }
}
