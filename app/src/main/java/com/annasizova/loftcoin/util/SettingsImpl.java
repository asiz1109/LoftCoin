package com.annasizova.loftcoin.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;


public class SettingsImpl implements Settings {

    private static final String KEY = "show_welcome_screen";
    private final SharedPreferences preferences;

    SettingsImpl(@NonNull Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public boolean shouldShowWelcomeScreen() {
        return preferences.getBoolean(KEY, true);
    }

    @Override
    public void doNotShowWelcomeScreen() {
        preferences.edit().putBoolean(KEY, false).apply();
    }
}
