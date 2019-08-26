package com.annasizova.loftcoin.main;

import androidx.collection.SparseArrayCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.annasizova.loftcoin.R;
import com.annasizova.loftcoin.util.Supplier;

import javax.inject.Inject;

class MainNavigator {

    private final FragmentActivity activity;
    private final SparseArrayCompat<Supplier<Fragment>> fragments;

    @Inject
    MainNavigator(FragmentActivity activity, SparseArrayCompat<Supplier<Fragment>> fragments) {
        this.activity = activity;
        this.fragments = fragments;
    }

    void navigateTo (int id) {
        final Supplier<Fragment> factory = fragments.get(id);
        if (factory != null) {
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, factory.get())
                    .commit();
        }
    }
}
