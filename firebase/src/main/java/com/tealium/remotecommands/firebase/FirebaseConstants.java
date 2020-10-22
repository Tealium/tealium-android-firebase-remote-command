package com.tealium.remotecommands.firebase;

public final class FirebaseConstants {

    public static final String TAG = "Tealium-Firebase";
    public static final String SEPARATOR = ",";

    public static final class Commands {
        private Commands() {
        }

        public static final String CONFIGURE = "config";
        public static final String LOG_EVENT = "logevent";
        public static final String SET_SCREEN_NAME = "setscreenname";
        public static final String SET_USER_ID = "setuserid";
        public static final String SET_USER_PROPERTY = "setuserproperty";
        public static final String RESET_DATA = "resetdata";
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
        //        public static final String EVENT_PARAMS = "firebase_event_params";
        public static final String SCREEN_NAME = "firebase_screen_name";
        public static final String SCREEN_CLASS = "firebase_screen_class";
        public static final String USER_PROPERTY_NAME = "firebase_property_name";
        public static final String USER_PROPERTY_VALUE = "firebase_property_value";
        public static final String USER_ID = "firebase_user_id";
    }
}
