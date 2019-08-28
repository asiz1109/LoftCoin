package com.annasizova.loftcoin.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.SparseArrayCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import com.annasizova.loftcoin.R;
import com.annasizova.loftcoin.converter.ConverterFragment;
import com.annasizova.loftcoin.rate.RateFragment;
import com.annasizova.loftcoin.wallets.WalletsFragment;
import com.annasizova.loftcoin.util.Supplier;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    @Inject MainNavigator mainNavigator;
    @Inject ViewModelProvider.Factory vmFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerMainComponent.builder().activity(this).build().inject(this);

        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.tool_bar));

        final MainViewModel mainViewModel = ViewModelProviders.of(this, vmFactory).get(MainViewModel.class);

        final BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_bar);
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            mainViewModel.submitSelectedId(menuItem.getItemId());
            return true;
        });

        mainViewModel.selectedTitle().observe(this, title -> Objects
                .requireNonNull(getSupportActionBar())
                .setTitle(title));

        mainViewModel.selectedId().observe(this, mainNavigator::navigateTo);
        mainViewModel.selectedId().observe(this, bottomNavigationView::setSelectedItemId);
    }

}
