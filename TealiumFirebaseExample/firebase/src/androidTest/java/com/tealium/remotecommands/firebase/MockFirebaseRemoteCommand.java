package com.tealium.remotecommands.firebase;

import android.app.Application;
import android.os.Bundle;

import org.json.JSONObject;

public class MockFirebaseRemoteCommand extends FirebaseRemoteCommand {

    public MockFirebaseRemoteCommand(Application application) {
        super(application);
    }

    public MockFirebaseRemoteCommand(Application application, String commandId, String description) {
        super(application, commandId, description);
    }

    public void setWrapper(FirebaseWrapper wrapper) {
        mFirebaseWrapper = wrapper;
    }
}
