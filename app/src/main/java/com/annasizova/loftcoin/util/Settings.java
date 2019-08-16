package com.annasizova.loftcoin.util;

import android.content.Context;

import androidx.annotation.NonNull;

public interface Settings {

    @NonNull
    static Settings of(@NonNull Context context){
        return new SettingsImpl(context);
    }

    boolean shouldShowWelcomeScreen();

    void doNotShowWelcomeScreen();
}
