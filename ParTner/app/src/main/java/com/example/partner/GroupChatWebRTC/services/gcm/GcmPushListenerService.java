package com.example.partner.GroupChatWebRTC.services.gcm;

import android.os.Bundle;
import android.util.Log;

import com.example.partner.Core.utils.SharedPrefsHelper;
import com.example.partner.Core.utils.constant.GcmConsts;
import com.example.partner.GroupChatWebRTC.services.CallService;
import com.google.android.gms.gcm.GcmListenerService;
import com.quickblox.users.model.QBUser;

public class GcmPushListenerService extends GcmListenerService {
    private static final String TAG = GcmPushListenerService.class.getSimpleName();

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString(GcmConsts.EXTRA_GCM_MESSAGE);
        Log.v(TAG, "From: " + from);
        Log.v(TAG, "Message: " + message);

        SharedPrefsHelper sharedPrefsHelper = SharedPrefsHelper.getInstance();
        if (sharedPrefsHelper.hasQbUser()) {
            Log.d(TAG, "App have logined user");
            QBUser qbUser = sharedPrefsHelper.getQbUser();
            startLoginService(qbUser);
        }
    }

    private void startLoginService(QBUser qbUser) {
        CallService.start(this, qbUser);
    }
}
