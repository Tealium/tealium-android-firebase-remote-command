package com.tealium.remotecommands.firebase;

public final class FirebaseConstants {

    private FirebaseConstants() {
    }

    public static final String TAG = "Tealium-Firebase";
    public static final String SEPARATOR = ",";

    public static final class Commands {
        private Commands() {
        }

        public static final String CONFIGURE = "config";
        public static final String LOG_EVENT = "logevent";
        public static final String SET_USER_ID = "setuserid";
        public static final String SET_USER_PROPERTY = "setuserproperty";
        public static final String RESET_DATA = "resetdata";
        public static final String SET_DEFAULT_PARAMETERS = "setdefaultparameters";
        public static final String SET_CONSENT = "setconsent";
    }

    public static final class Keys {
        private Keys() {
        }

        public static final String COMMAND_NAME = "command_name";

        public static final String SESSION_TIMEOUT = "firebase_session_timeout_seconds";
        public static final String MIN_SECONDS = "firebase_session_minimum_seconds";
        public static final String ANALYTICS_ENABLED = "firebase_analytics_enabled";
        public static final String LOG_LEVEL = "firebase_log_level";
        public static final String EVENT_NAME = "firebase_event_name";
        public static final String EVENT_PARAMS = "event";
        public static final String DEFAULT_PARAMS = "default";
        public static final String ITEMS_PARAMS = "items";
        public static final String TAG_EVENT_PARAMS = "firebase_event_params";
        public static final String TAG_DEFAULT_PARAMS = "firebase_default_params";
        public static final String SCREEN_NAME = "firebase_screen_name";
        public static final String SCREEN_CLASS = "firebase_screen_class";
        public static final String USER_PROPERTY_NAME = "firebase_property_name";
        public static final String USER_PROPERTY_VALUE = "firebase_property_value";
        public static final String USER_ID = "firebase_user_id";
        public static final String CONSENT_SETTINGS = "firebase_consent_settings";
    }

    public static final class ItemProperties {
        private ItemProperties() {
        }

        public static final String ID = "param_item_id";
        public static final String BRAND ="param_item_brand";
        public static final String CATEGORY ="param_item_category";
        public static final String NAME ="param_item_name";
        public static final String PRICE ="param_price";
        public static final String QUANTITY ="param_quantity";
        public static final String INDEX ="param_index";
        public static final String LIST ="param_item_list";
        public static final String LOCATION_ID ="param_item_location_id";
        public static final String VARIANT ="param_item_variant";
    }
}
