package com.annasizova.loftcoin;

import android.app.Application;
import android.os.StrictMode;

import com.annasizova.loftcoin.log.DebugTree;

import timber.log.Timber;

public class LoftApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            StrictMode.enableDefaults();
            Timber.plant(new DebugTree());
        }
        Timber.d("%s", this);
    }
}
