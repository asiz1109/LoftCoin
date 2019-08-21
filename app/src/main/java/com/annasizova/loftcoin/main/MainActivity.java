package com.annasizova.loftcoin.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.SparseArrayCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import com.annasizova.loftcoin.R;
import com.annasizova.loftcoin.converter.ConverterFragment;
import com.annasizova.loftcoin.rate.RateFragment;
import com.annasizova.loftcoin.wallets.WalletsFragment;
import com.annasizova.loftcoin.util.Supplier;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final SparseArrayCompat<Supplier<Fragment>> FRAGMENTS;

    static {
        FRAGMENTS = new SparseArrayCompat<>();
        FRAGMENTS.put(R.id.menu_wallets, WalletsFragment::new);
        FRAGMENTS.put(R.id.menu_rate, RateFragment::new);
        FRAGMENTS.put(R.id.menu_converter, ConverterFragment::new);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.tool_bar));

        final MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        final BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_bar);
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            mainViewModel.submitSelectedId(menuItem.getItemId());
            return true;
        });

        mainViewModel.selectedTitle().observe(this, title -> Objects
                .requireNonNull(getSupportActionBar())
                .setTitle(title));

        mainViewModel.selectedId().observe(this, this::replaceFragment);
    }

    private void replaceFragment(int itemId) {
        final Supplier <Fragment> factory = FRAGMENTS.get(itemId);
        if (factory != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, factory.get())
                    .commit();
        }
    }
}
