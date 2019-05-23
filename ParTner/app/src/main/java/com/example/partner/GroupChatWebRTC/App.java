package com.example.partner.GroupChatWebRTC;

import com.crashlytics.android.Crashlytics;
import com.example.partner.Core.CoreApp;
import com.example.partner.GroupChatWebRTC.util.QBResRequestExecutor;

import io.fabric.sdk.android.Fabric;

public class App extends CoreApp {
    private static App instance;
    private QBResRequestExecutor qbResRequestExecutor;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        initApplication();
    }

    private void initApplication() {
        instance = this;
    }

    public synchronized QBResRequestExecutor getQbResRequestExecutor() {
        return qbResRequestExecutor == null
                ? qbResRequestExecutor = new QBResRequestExecutor()
                : qbResRequestExecutor;
    }
}
