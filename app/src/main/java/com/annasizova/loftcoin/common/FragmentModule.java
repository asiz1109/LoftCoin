package com.annasizova.loftcoin.common;

import androidx.fragment.app.Fragment;

import com.annasizova.loftcoin.AppComponent;
import com.annasizova.loftcoin.data.Currencies;
import com.annasizova.loftcoin.rx.RxSchedulers;

import java.util.Locale;

import dagger.Module;
import dagger.Provides;
import dagger.Reusable;

@Module
public interface FragmentModule {

    @Provides
    @Reusable
    static AppComponent appComponent(Fragment fmt) {
        return AppComponent.from(fmt.requireContext());
    }

    @Provides
    static Currencies currencies(AppComponent appComponent) {
        return appComponent.currencies();
    }

    @Provides
    static Locale locale(AppComponent appComponent) {
        return appComponent.locale().get();
    }

    @Provides
    static RxSchedulers schedulers(AppComponent appComponent) {
        return appComponent.schedulers();
    }
}
