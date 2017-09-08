package com.cashkarodemoapp;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by admin on 8/26/2017.
 */

public class CashKaroApplication extends Application {
    public static FirebaseAnalytics mFirebaseAnalytics;
    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }
}
