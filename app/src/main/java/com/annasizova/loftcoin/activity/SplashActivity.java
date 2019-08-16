package com.annasizova.loftcoin.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;

import com.annasizova.loftcoin.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        new Handler().postDelayed(() -> {
            if (preferences.getBoolean("show_welcome_screen", true)) {
                startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
            } else {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
        }, 2000);
    }
}
