package com.example.ListBase;

import android.app.Application;

import androidx.multidex.MultiDexApplication;

import com.onesignal.OneSignal;

public class ApplicationClass extends MultiDexApplication {
    private static final String ONESIGNAL_APP_ID = "dc38c1bf-8af0-4c0d-a038-d0f2ae08feea";
    @Override
    public void onCreate() {
        super.onCreate();
        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
    }
}
