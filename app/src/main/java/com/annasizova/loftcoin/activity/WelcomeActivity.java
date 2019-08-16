package com.annasizova.loftcoin.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.annasizova.loftcoin.R;
import com.annasizova.loftcoin.adapter.WelcomeAdapter;
import com.annasizova.loftcoin.util.Settings;

import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator;

public class WelcomeActivity extends AppCompatActivity {

    TextView tvStart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        final RecyclerView welcomeRecyclerView = findViewById(R.id.welcome_recycler_view);
        welcomeRecyclerView.setAdapter(new WelcomeAdapter());
        welcomeRecyclerView.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false));
        ScrollingPagerIndicator recyclerIndicator = findViewById(R.id.indicator);
        recyclerIndicator.attachToRecyclerView(welcomeRecyclerView);
        recyclerIndicator.setDotColor(getResources().getColor(R.color.grey_blue));
        recyclerIndicator.setSelectedDotColor(getResources().getColor(R.color.white));
        new PagerSnapHelper().attachToRecyclerView(welcomeRecyclerView);

        tvStart = findViewById(R.id.tv_start);
        tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Settings settings = Settings.of(WelcomeActivity.this);
                settings.doNotShowWelcomeScreen();
                final Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
}
