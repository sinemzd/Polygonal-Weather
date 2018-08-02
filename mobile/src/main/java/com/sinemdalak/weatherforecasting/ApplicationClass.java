package com.sinemdalak.weatherforecasting;

import android.app.Application;
import com.onesignal.OneSignal;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class ApplicationClass extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());


        //OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();


    }
}