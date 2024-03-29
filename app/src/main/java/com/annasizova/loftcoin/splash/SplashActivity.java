package com.annasizova.loftcoin.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.annasizova.loftcoin.R;
import com.annasizova.loftcoin.main.MainActivity;
import com.annasizova.loftcoin.util.Settings;
import com.annasizova.loftcoin.welcome.WelcomeActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final Settings settings = Settings.of(this);
        new Handler().postDelayed(() -> {
            if (settings.shouldShowWelcomeScreen()) {
                startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
            } else {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
        }, 2000);
    }
}
