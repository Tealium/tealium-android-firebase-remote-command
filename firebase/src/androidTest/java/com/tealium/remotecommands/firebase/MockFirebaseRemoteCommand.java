package com.tealium.remotecommands.firebase;

import android.app.Application;

public class MockFirebaseRemoteCommand extends FirebaseRemoteCommand {

    public MockFirebaseRemoteCommand(Application application) {
        super(application);
    }

    public MockFirebaseRemoteCommand(Application application, String commandId, String description) {
        super(application, commandId, description);
    }

    public void setCommand(FirebaseCommand command) {
        mFirebaseCommand = command;
    }
}
