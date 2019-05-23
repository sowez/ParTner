package com.example.partner.Core.gcm;

import android.os.Bundle;
import android.util.Log;

import com.example.partner.Core.utils.ActivityLifecycle;
import com.quickblox.messages.services.gcm.QBGcmPushListenerService;

public abstract class CoreGcmPushListenerService extends QBGcmPushListenerService {
    private static final String TAG = CoreGcmPushListenerService.class.getSimpleName();

    @Override
    public void sendPushMessage(Bundle data, String from, String message) {
        super.sendPushMessage(data, from, message);
        Log.v(TAG, "From: " + from);
        Log.v(TAG, "Message: " + message);

        if (ActivityLifecycle.getInstance().isBackground()) {
            showNotification(message);
        }
    }

    protected abstract void showNotification(String message);
}
